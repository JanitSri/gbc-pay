package com.COMP3095.gbc_pay.repositories;


import com.COMP3095.gbc_pay.models.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository("verificationTokenRepository")
public interface VerificationTokenRepository extends TokenRepository<VerificationToken> {
}
