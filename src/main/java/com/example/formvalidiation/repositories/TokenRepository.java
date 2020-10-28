package com.example.formvalidiation.repositories;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByVerifiedToken(String token);
}
