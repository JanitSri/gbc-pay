package com.COMP3095.gbc_pay.services.token;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.Token;

abstract class TokenService<T extends Token> {
    public abstract T createToken(Profile profile);

    public abstract T validateToken(String token);

    public abstract T getByProfile(Profile profile);
}
