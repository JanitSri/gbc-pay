package com.COMP3095.gbc_pay.repositories;


import com.COMP3095.gbc_pay.models.PasswordResetToken;
import org.springframework.stereotype.Repository;

@Repository("passwordResetTokenRepository")
public interface PasswordResetTokenRepository extends TokenRepository<PasswordResetToken> {
}
