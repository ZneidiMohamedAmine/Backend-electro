package org.egh.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Role;
import org.egh.demo.dto.EmailRequest;
import org.egh.demo.dto.PasswordDto;
import org.egh.demo.dto.SocialLoginRequest;
import org.egh.demo.entity.Utilisateur;
import org.egh.demo.entity.VerificationToken;
import org.egh.demo.repository.IUtilisateurRepository;
import org.egh.demo.repository.PasswordResetTokenRepository;
import org.egh.demo.repository.VerificationTokenRepository;
import org.egh.demo.service.UtilisateurService;
import org.egh.demo.service.impl.EmailService;
import org.egh.demo.service.impl.JwtUtility;
import org.egh.demo.service.impl.UtilisateurServiceImpl;
import org.egh.demo.service.impl.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private IUtilisateurRepository userRepository;


    @Autowired
    private UtilisateurServiceImpl userService;



    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UtilisateurController userController;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody Utilisateur user) {
        Map<String, String> response = new HashMap<>();
        try {
            log.info("Signup attempt for user: {}", user.getNom());

            user.setMotdepasse(encoder.encode(user.getMotdepasse()));

            userController.createUtilisateur(user);
            Map<String, String> claims = new HashMap<>();
            
            claims.put("email", user.getEmail());
            claims.put("nom", user.getNom() != null ? user.getNom() : "");

            // Use email as the subject since it's guaranteed to be unique and present
            String token = jwtUtility.generateToken(claims, user.getEmail(), 24 * 60 * 60 * 1000);

            response.put("message", "Utilisateur registered successfully");
            response.put("token", token);
        } catch (Exception e) {
            log.error("Signup failed for user: {}. Reason: {}", user.getNom(), e.getMessage(), e);
            response.put("error", "Signup failed: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Utilisateur loginUtilisateur) {
        Map<String, String> response = new HashMap<>();
        try {
            log.info("Login attempt for user: {}", loginUtilisateur.getEmail());
            log.info("Login attempt for user password: {}", loginUtilisateur.getMotdepasse());
            log.info("Login attempt for user password encoded: {}", encoder.encode(loginUtilisateur.getMotdepasse()));
            Utilisateur userOptional = userRepository.findByEmail(loginUtilisateur.getEmail());


            if (userOptional != null && encoder.matches(loginUtilisateur.getMotdepasse(), userOptional.getMotdepasse()))
            {
                Map<String, String> claims = new HashMap<>();

                claims.put("userid", String.valueOf(userOptional.getId()));
                claims.put("email", userOptional.getEmail());
                claims.put("nom", userOptional.getNom() != null ? userOptional.getNom() : "");

                // Use email as the subject since it's guaranteed to be unique and present
                String token = jwtUtility.generateToken(claims, userOptional.getEmail(), 24 * 60 * 60 * 1000);
                response.put("jwtToken", token);
                log.info("Login successful for user: {}", loginUtilisateur.getEmail());
            } else {
                log.warn("Login failed. Invalid credentials for user: {}", loginUtilisateur.getEmail());
                response.put("error", "Invalid credentials");
            }
        } catch (Exception e) {
            log.error("Login error for user: {}. Reason: {}", loginUtilisateur.getEmail(), e.getMessage(), e);
            response.put("error", "Login failed");
        }
        return response;
    }

    @PostMapping("/verifyEmail")
    public Map<String, String> Verifyaccount(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        Map<String, String> response = new HashMap<>();
        try {
            log.info("Password reset requested for email: {}", email);
            Utilisateur user = userRepository.findByEmail(email);

            if (user == null) {
                log.warn("Password reset failed. No user found for email: {}", email);
                response.put("message", "Utilisateur not Found!");
                return response;
            }

            String token = UUID.randomUUID().toString();
            userService.createVerificationTokenForUser(user, token);
            emailService.sendVerificationEmail(email, token);
            response.put("message", "We've sent a verifcation mail!");
            log.info("Verification email sent to: {}", email);

        } catch (Exception e) {
            log.error("Verifcation error for email: {}. Reason: {}", email, e.getMessage(), e);
            response.put("error", "Failed to send verification email");
        }
        return response;
    }

    @PostMapping("/verifyEmailToken")
    public Map<String, String> verifyEmailToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, String> response = new HashMap<>();
        
        try {
            log.info("Email verification requested with token: {}", token);
            String result = verificationService.validateVerificationToken(token);

            if (result != null) {
                log.warn("Email verification failed: {}", result);
                if (result.equals("expired")) {
                    response.put("error", "Token expired");
                } else {
                    response.put("error", "Invalid token");
                }
                return response;
            }

            // Token is valid, mark user as verified and delete the token
            VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
            if (verificationToken != null) {
                Utilisateur user = verificationToken.getUser();
                // You might want to add a verified field to your User entity
                // user.setVerified(true);
                // userRepository.save(user);
                
                // Delete the verification token
                verificationTokenRepository.delete(verificationToken);
                
                response.put("message", "Email verified successfully");
                log.info("Email verification successful for user: {}", user.getEmail());
            } else {
                response.put("error", "Token not found");
            }

        } catch (Exception e) {
            log.error("Error during email verification. Token: {}, Reason: {}", token, e.getMessage(), e);
            response.put("error", "Verification failed");
        }
        
        return response;
    }

    @PostMapping("/resetPassword")
    public Map<String, String> resetPassword(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        Map<String, String> response = new HashMap<>();
        try {
            log.info("Password reset requested for email: {}", email);
            Utilisateur user = userRepository.findByEmail(email);

            if (user == null) {
                log.warn("Password reset failed. No user found for email: {}", email);
                response.put("message", "Utilisateur not Found!");
                return response;
            }

            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            emailService.sendRecoveryEmail(email, token);
            response.put("message", "We've sent a recovery mail!");
            log.info("Recovery email sent to: {}", email);

        } catch (Exception e) {
            log.error("Password reset error for email: {}. Reason: {}", email, e.getMessage(), e);
            response.put("error", "Failed to send recovery email");
        }
        return response;
    }

    @GetMapping("/changePassword")
    public RedirectView redirectToResetPasswordPage(@RequestParam("token") String token) {
        try {
            log.info("Change password requested with token: {}", token);
            String result = verificationService.validatePasswordResetToken(token);

            if (result != null) {
                log.warn("Token validation failed: {}", result);
                return new RedirectView("http://localhost:4200/#/auth/error");
            } else {
                // Don't delete the token here - it will be deleted after password is actually changed
                return new RedirectView("http://localhost:4200/save-password?token=" + token);
            }
        } catch (Exception e) {
            log.error("Error during token validation. Token: {}, Reason: {}", token, e.getMessage(), e);
            return new RedirectView("http://localhost:4200/error/server");
        }
    }


    @PostMapping("/savePassword")
    public Map<String, String> savePassword(@RequestBody PasswordDto passwordDto) {
        Map<String, String> response = new HashMap<>();
        try {
            log.info("Attempt to save new password with token: {}", passwordDto.getToken());

            if (passwordDto.getToken() == null) {
                log.warn("Null token in password save request");
                response.put("error", "Token cannot be null");
                return response;
            }

            String result = verificationService.validatePasswordResetToken(passwordDto.getToken());

            if (result != null) {
                log.warn("Token validation failed: {}", result);
                if (result.equals("expired")) {
                    response.put("error", "Token expired");
                } else {
                    response.put("error", "Invalid token");
                }
                return response;
            }

            Utilisateur user = userService.getUtilisateurByPasswordResetToken(passwordDto.getToken());

            if (user != null) {
                userService.changeUtilisateurPassword(user, passwordDto.getNewPassword(), passwordDto.getToken());
                response.put("message", "Password changed successfully!");
                log.info("Password changed successfully for user: {}", user.getEmail());
            } else {
                log.warn("No user found for token: {}", passwordDto.getToken());
                response.put("error", "Invalid token");
            }

        } catch (Exception e) {
            log.error("Error saving password. Token: {}, Reason: {}", passwordDto.getToken(), e.getMessage(), e);
            response.put("error", "Internal server error");
        }
        return response;
    }

  /*  @PostMapping("/social-login")
    public ResponseEntity<Void> socialLogin(@RequestBody SocialLoginRequest request) {
        // 1. Verify Google token & load/create user…
        GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyToken(request.getCredential());
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = payload.getEmail();
        Utilisateur user = userRepository.findByEmail(email);
        if (user == null) {
            Utilisateur newUtilisateur = new Utilisateur();
            newUtilisateur.setEmail(email);
            newUtilisateur.setName((String) payload.get("name"));// Optional default role
            user = userRepository.save(newUtilisateur);  // <-- Fix: assign saved user back to user
        }

        // 2. Generate your JWT

        Map<String,String> claims = Map.of("role", user.getRoles().toString());
        String jwt = jwtUtility.generateToken(claims, email, 86_400_000);

        // 3. Build a Set-Cookie header for an HttpOnly, Secure cookie
        ResponseCookie cookie = ResponseCookie.from("JWT", jwt)                  // cookie name & value
                .httpOnly(true)                                                      // not accessible in JS :contentReference[oaicite:0]{index=0}
                .secure(true)                                                        // only over HTTPS (in prod)
                .path("/")                                                           // sent to all paths
                .maxAge(86_400)                                                      // 1 day in seconds
                .sameSite("Strict")                                                  // mitigate CSRF
                .build();

        // 4. Redirect to your Angular app’s root URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())                   // set the JWT cookie :contentReference[oaicite:1]{index=1}
                .location(URI.create("http://localhost:4200/"))                      // 302 → Angular root
                .build();
    }*/


}

