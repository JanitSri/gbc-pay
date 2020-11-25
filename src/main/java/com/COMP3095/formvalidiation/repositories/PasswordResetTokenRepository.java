package com.COMP3095.formvalidiation.repositories;


import com.COMP3095.formvalidiation.models.PasswordResetToken;
import org.springframework.stereotype.Repository;

@Repository("passwordResetTokenRepository")
public interface PasswordResetTokenRepository extends TokenRepository<PasswordResetToken> {
}
