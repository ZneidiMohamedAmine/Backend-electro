package org.egh.demo.repository;


import org.egh.demo.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface IUtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByNom(String userNom);

    Utilisateur findByEmail(String email);
}
