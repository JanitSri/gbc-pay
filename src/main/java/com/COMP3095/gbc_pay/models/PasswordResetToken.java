/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Class for the Password Reset Token entity.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PasswordResetToken extends Token{

    private boolean expired;

    @ManyToOne
    private Profile profile;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String tokenName, LocalDate createdDate, boolean expired) {
        super(tokenName, createdDate);
        this.expired = expired;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
