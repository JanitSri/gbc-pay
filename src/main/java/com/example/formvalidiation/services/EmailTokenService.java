package com.example.formvalidiation.services;

import com.example.formvalidiation.models.EmailToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.EmailTokenRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;

    public EmailTokenService(EmailTokenRepository verificationTokenRepository) {
        this.emailTokenRepository = verificationTokenRepository;
    }

    public EmailToken createToken(User user){
        final EmailToken verificationToken = new EmailToken(UUID.randomUUID().toString(), LocalDate.now());
        verificationToken.setUser(user);
        emailTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public EmailToken validateToken(String verificationToken){
        Optional<EmailToken> optionalToken = emailTokenRepository.findByTokenName(verificationToken);

        return optionalToken.orElse(null);
    }
}
