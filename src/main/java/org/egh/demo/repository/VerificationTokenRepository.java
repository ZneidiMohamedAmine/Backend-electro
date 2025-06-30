package org.egh.demo.repository;


import org.egh.demo.entity.PasswordResetToken;
import org.egh.demo.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("SELECT t FROM VerificationToken t WHERE t.token = :token")
    VerificationToken findByToken(@Param("token") String token);

}
