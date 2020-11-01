package com.example.formvalidiation.repositories;


import com.example.formvalidiation.models.EmailToken;
import com.example.formvalidiation.models.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTokenRepository extends CrudRepository<EmailToken, Long> {
    Optional<EmailToken> findByTokenName(String token);
}
