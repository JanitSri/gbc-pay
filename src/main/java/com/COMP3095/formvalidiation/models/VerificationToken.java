/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Verification token that is used to verify emails.
 *********************************************************************************/

package com.COMP3095.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class VerificationToken extends Token {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(String tokenName, LocalDate createdDate) {
        super(tokenName, createdDate);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
