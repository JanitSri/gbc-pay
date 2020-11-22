/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Service class to handle the password reset token from the repository.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.token;

import com.COMP3095.formvalidiation.models.PasswordResetToken;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.repositories.TokenRepository;
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
