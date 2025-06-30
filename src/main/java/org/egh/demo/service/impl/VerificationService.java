package org.egh.demo.service.impl;


import org.egh.demo.entity.PasswordResetToken;
import org.egh.demo.entity.UserVerification;
import org.egh.demo.entity.VerificationToken;
import org.egh.demo.repository.PasswordResetTokenRepository;
import org.egh.demo.repository.UserVerificationRepository;
import org.egh.demo.repository.VerificationTokenRepository;
import org.egh.demo.service.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);

    @Autowired
    private UserVerificationRepository verificationRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public boolean verifyCode(String email, String code) {
        UserVerification userVerification = verificationRepository.findByEmail(email);
        if (userVerification != null && userVerification.getVerificationCode().equals(code)) {
            // Verification successful
            return true;
        }
        // Verification failed
        return false;
    }

    public String validatePasswordResetToken(String token) {
        String cleanedToken = cleanToken(token);
        logger.debug("Validating password reset token: {}", cleanedToken);

        final PasswordResetToken passToken = passwordTokenRepository.findByToken(cleanedToken);

        // It's good practice to log the token details *before* the check
        if (passToken != null) {
            logger.debug("Found PasswordResetToken with token: {} and expiryDate: {}", passToken.getToken(), passToken.getExpiryDate());
        } else {
            logger.warn("PasswordResetToken not found for token: {}", cleanedToken);
        }

        return validateToken(passToken);
    }

    public String validateVerificationToken(String token) {
        String cleanedToken = cleanToken(token);
        logger.debug("Validating verification token: {}", cleanedToken);

        final VerificationToken verifToken = verificationTokenRepository.findByToken(cleanedToken);

        // Log token details
        if (verifToken != null) {
            logger.debug("Found VerificationToken with token: {} and expiryDate: {}", verifToken.getToken(), verifToken.getExpiryDate());
        } else {
            logger.warn("VerificationToken not found for token: {}", cleanedToken);
        }

        return validateToken(verifToken);
    }

    // --- Private Helper Methods ---

    /**
     * Cleans the input token by trimming whitespace and removing newline characters.
     * @param token The raw token string.
     * @return The cleaned token string.
     */
    private String cleanToken(String token) {
        if (token == null) {
            return ""; // Or throw an IllegalArgumentException if null tokens are not allowed
        }
        return token.trim().replaceAll("[\\r\\n]+", "");
    }

    /**
     * Validates a generic Token (either PasswordResetToken or VerificationToken).
     * This method leverages the `Token` interface.
     * @param token The Token object to validate.
     * @return "invalidToken" if not found, "expired" if expired, or null if valid.
     */
    private String validateToken(Token token) {
        if (!isTokenFound(token)) {
            logger.warn("Token validation failed: Token not found.");
            return "invalidToken";
        }
        if (isTokenExpired(token)) {
            logger.warn("Token validation failed: Token expired for token: {}", token.getToken());
            return "expired";
        }
        logger.info("Token validation successful for token: {}", token.getToken());
        return null; // Token is valid
    }

    /**
     * Checks if a token object is not null.
     * @param token The Token object.
     * @return true if the token is found (not null), false otherwise.
     */
    private boolean isTokenFound(Token token) {
        return token != null;
    }

    /**
     * Checks if a token's expiry date is before the current time.
     * @param token The Token object to check.
     * @return true if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(Token token) {
        final Calendar cal = Calendar.getInstance();
        return token.getExpiryDate().before(cal.getTime());
    }
}
