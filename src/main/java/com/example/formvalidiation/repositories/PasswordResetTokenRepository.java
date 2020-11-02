package com.example.formvalidiation.repositories;



import com.example.formvalidiation.models.PasswordResetToken;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetTokenRepository extends TokenRepository<PasswordResetToken> {
}
