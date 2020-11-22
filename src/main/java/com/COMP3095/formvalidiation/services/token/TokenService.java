/********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Token service that defines the requirements for other token classes.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.token;

import com.COMP3095.formvalidiation.models.Token;
import com.COMP3095.formvalidiation.models.User;

abstract class TokenService<T extends Token> {
    public abstract T createToken(User user);

    public abstract T validateToken(String token);

    public abstract T getByUser(User user);
}
