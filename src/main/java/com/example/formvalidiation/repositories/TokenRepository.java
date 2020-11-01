package com.example.formvalidiation.repositories;

import com.example.formvalidiation.models.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends Token> extends CrudRepository<T, Long> {
    Optional<T> findByTokenName(String token);
}
