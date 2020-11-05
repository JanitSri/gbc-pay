package com.example.formvalidiation.services.token;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.PasswordResetTokenRepository;
import com.example.formvalidiation.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService extends TokenService<PasswordResetToken>{
    private final TokenRepository tokenRepository;

    public PasswordResetTokenService(@Qualifier("passwordResetTokenRepository") TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public PasswordResetToken createToken(User user){
        final PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), LocalDate.now(), false);
        passwordResetToken.setUser(user);
        tokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    @Override
    public PasswordResetToken validateToken(String verificationToken){
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByTokenName(verificationToken);
        return optionalToken.orElse(null);
    }

    public PasswordResetToken getByUser(User user){
        List<PasswordResetToken> tokens = tokenRepository.findByUser(user);
        PasswordResetToken token = tokens.stream()
                .filter(passwordResetToken -> !passwordResetToken.isExpired())
                .findFirst()
                .orElse(null);
        return token;
    }
}
