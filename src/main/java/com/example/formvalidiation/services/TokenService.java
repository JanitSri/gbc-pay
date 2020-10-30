package com.example.formvalidiation.services;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(User user){
        final Token token = new Token(UUID.randomUUID().toString(), LocalDate.now());
        token.setUser(user);
        tokenRepository.save(token);
        return token;
    }

    public Token validateToken(String token){
        Optional<Token> optionalToken = tokenRepository.findByTokenName(token);

        return optionalToken.orElse(null);
    }
}
