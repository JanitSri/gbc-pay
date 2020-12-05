/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Token service that defines the requirements for other token classes.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.services.token;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.Token;

abstract class TokenService<T extends Token> {
    public abstract T createToken(Profile profile);

    public abstract T validateToken(String token);

    public abstract T getByProfile(Profile profile);
}
