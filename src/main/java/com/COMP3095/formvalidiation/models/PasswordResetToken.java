/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Token that is used with password resets.
 *********************************************************************************/

package com.COMP3095.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PasswordResetToken extends Token{

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String tokenName, LocalDate createdDate, boolean expired) {
        super(tokenName, createdDate);
        this.expired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
