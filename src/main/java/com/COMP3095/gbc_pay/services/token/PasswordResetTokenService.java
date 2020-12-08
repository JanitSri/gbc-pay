package com.COMP3095.gbc_pay.services.token;

import com.COMP3095.gbc_pay.models.PasswordResetToken;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.repositories.TokenRepository;
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
    public PasswordResetToken createToken(Profile profile){
        final PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), LocalDate.now(), false);
        passwordResetToken.setProfile(profile);

        return passwordResetToken;
    }

    @Override
    public PasswordResetToken validateToken(String verificationToken){
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByTokenName(verificationToken);
        return optionalToken.orElse(null);
    }

    public PasswordResetToken getByProfile(Profile profile){
        List<PasswordResetToken> tokens = tokenRepository.findByProfile(profile);
        return tokens.stream()
                .filter(passwordResetToken -> !passwordResetToken.isExpired())
                .findFirst()
                .orElse(null);
    }
}
