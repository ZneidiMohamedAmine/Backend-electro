package org.egh.demo.config;


import org.egh.demo.repository.IUtilisateurRepository;
import org.egh.demo.service.impl.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final IUtilisateurRepository userRepository; // Assuming this is needed for UserDetailsService

    public SecurityConfiguration(JwtFilter jwtFilter, IUtilisateurRepository userRepository) {
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }

    // You likely need a UserDetailsService and PasswordEncoder for your form login
    // and potentially for JWT validation depending on your JWT setup.


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    // --- 1. Security Filter Chain for Authentication Endpoints ---
    @Bean
    @Order(0) // This must be the highest priority (lowest order number)
    public SecurityFilterChain authEndpointsSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .securityMatcher("/auth/**") // Only apply to /auth/**
                .csrf(csrf -> csrf.disable()) // Disable CSRF for auth endpoints if stateless
                .authorizeHttpRequests(request -> {
                    request.anyRequest().permitAll(); // Permit all requests to /auth/**
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Auth endpoints are typically stateless
                .build();
    }

    // --- 2. Security Filter Chain for API Endpoints (JWT) ---
    @Bean
    @Order(1) // Next priority
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .securityMatcher("/api/**") // Only apply to /api/**
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints if stateless
                .authorizeHttpRequests(request -> {
                    request.anyRequest().authenticated(); // All /api/** requests must be authenticated
                })
                .oauth2ResourceServer(rs -> rs.jwt()) // Configure JWT resource server
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add your JWT filter
                .build();
    }

    // --- 3. Security Filter Chain for Web Pages (Form Login) ---
    // This will act as the default for any paths not matched by the above two.
    @Bean
    @Order(2) // Lowest priority (highest order number)
    public SecurityFilterChain formSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // You might want to enable CSRF for form-based login if it's session-based
                // For simple web pages that don't need CSRF, disabling is fine
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/", "/error", "/open").permitAll(); // Public web pages
                    request.anyRequest().authenticated(); // All other web pages require authentication
                })
                .formLogin((formLoginConfig) -> formLoginConfig.defaultSuccessUrl("/protected", true))
                .logout(logoutConfig -> logoutConfig.logoutSuccessUrl("/"))
                .build();
    }

    // Ignore selected URIs from security checks
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Ignore static directories from Security Filter Chain
        return web -> web.ignoring().requestMatchers("/images/**", "/js/**");
    }

    // Custom User Details Service to manage login
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = (userName) -> {
            return userRepository.findByNom(userName);
        };
        return userDetailsService;
    }

    // Password encoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtUtility jwtUtility) {
        SecretKey key = jwtUtility.getSignInKey(); // make sure getSignInKey() is public
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

}
