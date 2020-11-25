package com.COMP3095.formvalidiation.repositories;


import com.COMP3095.formvalidiation.models.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository("verificationTokenRepository")
public interface VerificationTokenRepository extends TokenRepository<VerificationToken> {
}
