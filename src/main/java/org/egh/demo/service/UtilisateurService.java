package org.egh.demo.service;

import org.egh.demo.entity.Utilisateur;
import java.util.List;

public interface UtilisateurService {
    Utilisateur save(Utilisateur utilisateur);
    List<Utilisateur> findAll();
    Utilisateur findById(Long id);
    Utilisateur update(Long id, Utilisateur utilisateur);
    void delete(Long id);

    Utilisateur findUserByEmail(String email);

    void createPasswordResetTokenForUser(Utilisateur user, String token);

    void createVerificationTokenForUser(Utilisateur user, String token);

    Utilisateur getUtilisateurByPasswordResetToken(String token);

    Utilisateur getUserByPasswordResetToken(String token);
} 