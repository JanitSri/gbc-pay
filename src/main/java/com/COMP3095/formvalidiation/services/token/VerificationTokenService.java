/********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Verification token service that allows access to the verification token repository.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.token;

import com.COMP3095.formvalidiation.models.Profile;
import com.COMP3095.formvalidiation.models.VerificationToken;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService extends TokenService<VerificationToken> {
    private final TokenRepository tokenRepository;

    public VerificationTokenService(@Qualifier("verificationTokenRepository") TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public VerificationToken createToken(Profile profile) {
        final VerificationToken verificationToken = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());
        verificationToken.setProfile(profile);
        return verificationToken;
    }

    @Override
    public VerificationToken validateToken(String verificationToken) {
        Optional<VerificationToken> optionalToken = tokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }

    @Override
    public VerificationToken getByProfile(Profile profile) {
        List<VerificationToken> tokens = tokenRepository.findByProfile(profile);
        return tokens.stream()
                .findFirst()
                .orElse(null);
    }
}
