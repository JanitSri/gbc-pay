package com.example.formvalidiation.services;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.VerificationToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.TokenRepository;
import com.example.formvalidiation.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private final TokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken createToken(User user){
        final VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public VerificationToken validateToken(String verificationToken){
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }
}
