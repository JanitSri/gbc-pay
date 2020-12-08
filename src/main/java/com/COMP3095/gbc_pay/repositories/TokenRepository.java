package com.COMP3095.gbc_pay.repositories;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends Token> extends CrudRepository<T, Long> {
    Optional<T> findByTokenName(String token);

    List<T> findByProfile(Profile profile);
}
