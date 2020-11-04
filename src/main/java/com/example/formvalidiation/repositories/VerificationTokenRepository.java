package com.example.formvalidiation.repositories;


import com.example.formvalidiation.models.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository("verificationTokenRepository")
public interface VerificationTokenRepository extends TokenRepository<VerificationToken> {
}
