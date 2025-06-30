package org.egh.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egh.demo.service.Token;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken implements Token {

    private static final int EXPIRATION = 60 * 48;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Utilisateur.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Utilisateur user;

    private Date expiryDate;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    public PasswordResetToken(String token, Utilisateur user) {
        this.token = token;
        this.user = user;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, EXPIRATION);
        this.expiryDate = calendar.getTime();
    }


}
