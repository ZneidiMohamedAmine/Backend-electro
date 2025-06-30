package org.egh.demo.service.impl;

import org.antlr.v4.runtime.misc.NotNull;
import org.egh.demo.entity.PasswordResetToken;
import org.egh.demo.entity.Utilisateur;
import org.egh.demo.entity.VerificationToken;
import org.egh.demo.repository.PasswordResetTokenRepository;
import org.egh.demo.repository.UtilisateurRepository;
import org.egh.demo.repository.VerificationTokenRepository;
import org.egh.demo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur not found with id: " + id));
    }

    @Override
    public Utilisateur update(Long id, Utilisateur utilisateur) {
        Utilisateur existingUtilisateur = findById(id);
        existingUtilisateur.setNom(utilisateur.getNom());
        existingUtilisateur.setPrenom(utilisateur.getPrenom());
        existingUtilisateur.setEmail(utilisateur.getEmail());
        existingUtilisateur.setRole(utilisateur.getRole());
        return utilisateurRepository.save(existingUtilisateur);
    }

    @Override
    public void delete(Long id) {
        utilisateurRepository.deleteById(id);
    }


    public void changeUtilisateurPassword(@NotNull Utilisateur user, String password, String token) {
        user.setMotdepasse(passwordEncoder.encode(password));
        utilisateurRepository.save(user);
        PasswordResetToken deletedToken = passwordTokenRepository.findByToken(token);
        passwordTokenRepository.delete(deletedToken);
    }



    @Override
    public Utilisateur findUserByEmail(String email) {
        String cleanedEmail = email.trim().replaceAll("[\\r\\n]+", "");
        System.out.println("Looking for email: " + cleanedEmail);

        Utilisateur user = utilisateurRepository.findByEmail(cleanedEmail.toLowerCase());

        if (user == null) {
            System.out.println("Utilisateur with email " + cleanedEmail + " not found!");
        } else {
            System.out.println("Utilisateur found: " + user.getEmail());
        }
        return user;
    }


    @Override
    public void createPasswordResetTokenForUser(Utilisateur user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public void createVerificationTokenForUser(Utilisateur user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public Utilisateur getUtilisateurByPasswordResetToken(String token) {
        PasswordResetToken tokenObj = passwordResetTokenRepository.findByToken(token);
        if (tokenObj != null) {
            return utilisateurRepository.findById(tokenObj.getUser().getId()).orElse(null);
        }
        return null;
    }

    @Override
    public Utilisateur getUserByPasswordResetToken(String token) {
        PasswordResetToken tokenObj = passwordResetTokenRepository.findByToken(token);
        if (tokenObj != null) {
            return utilisateurRepository.findById(tokenObj.getUser().getId()).orElse(null);
        }
        return null;
    }
} 