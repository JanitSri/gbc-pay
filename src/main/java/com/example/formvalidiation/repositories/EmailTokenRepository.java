package com.example.formvalidiation.repositories;



import com.example.formvalidiation.models.EmailToken;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailTokenRepository extends TokenRepository<EmailToken> {
}
