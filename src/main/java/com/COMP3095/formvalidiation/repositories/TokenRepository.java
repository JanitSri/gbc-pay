package com.COMP3095.formvalidiation.repositories;

import com.COMP3095.formvalidiation.models.Profile;
import com.COMP3095.formvalidiation.models.Token;
import com.COMP3095.formvalidiation.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TokenRepository<T extends Token> extends CrudRepository<T, Long> {
    Optional<T> findByTokenName(String token);

    List<T> findByProfile(Profile profile);
}
