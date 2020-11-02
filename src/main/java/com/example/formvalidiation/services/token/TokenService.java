package com.example.formvalidiation.services.token;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;

abstract class TokenService<T extends Token> {
    public abstract T createToken(User user);
    public abstract T validateToken(String token);
}
