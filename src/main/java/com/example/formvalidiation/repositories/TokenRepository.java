package com.example.formvalidiation.repositories;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends Token> extends CrudRepository<T, Long> {
    Optional<T> findByTokenName(String token);
    List<T> findByUser(User user);
}
