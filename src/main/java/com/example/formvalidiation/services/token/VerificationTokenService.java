package com.example.formvalidiation.services.token;

import com.example.formvalidiation.models.VerificationToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService extends TokenService<VerificationToken>{
    private final TokenRepository tokenRepository;

    public VerificationTokenService(@Qualifier("verificationTokenRepository") TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public VerificationToken createToken(User user){
        final VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public VerificationToken validateToken(String verificationToken){
        Optional<VerificationToken> optionalToken = tokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }

    @Override
    public VerificationToken getByUser(User user) {
        List<VerificationToken> tokens = tokenRepository.findByUser(user);
        VerificationToken token = tokens.stream()
                .findFirst()
                .orElse(null);
        return token;
    }
}
