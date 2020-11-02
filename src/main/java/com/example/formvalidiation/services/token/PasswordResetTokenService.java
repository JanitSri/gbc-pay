package com.example.formvalidiation.services.token;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService extends TokenService<PasswordResetToken>{
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository verificationTokenRepository) {
        this.passwordResetTokenRepository = verificationTokenRepository;
    }

    @Override
    public PasswordResetToken createToken(User user){
        final PasswordResetToken passwordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), LocalDate.now(), false);
        passwordResetToken.setUser(user);
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    @Override
    public PasswordResetToken validateToken(String verificationToken){
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }
}
