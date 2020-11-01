package com.example.formvalidiation.services;

import com.example.formvalidiation.models.EmailToken;
import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.EmailTokenRepository;
import com.example.formvalidiation.repositories.TokenRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTokenService {
    private final TokenRepository emailTokenRepository;

    public EmailTokenService(EmailTokenRepository verificationTokenRepository) {
        this.emailTokenRepository = verificationTokenRepository;
    }

    public EmailToken createEmailToken(User user){
        final EmailToken emailToken = new EmailToken(UUID.randomUUID().toString(), LocalDate.now(), false);
        emailToken.setUser(user);
        emailTokenRepository.save(emailToken);
        return emailToken;
    }

    public EmailToken validateEmailToken(String verificationToken){
        Optional<EmailToken> optionalToken = emailTokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }
}
