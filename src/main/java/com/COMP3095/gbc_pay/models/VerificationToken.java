/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Class for the Verification Token entity.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class VerificationToken extends Token {

    @OneToOne
    private Profile profile;

    public VerificationToken() {
    }

    public VerificationToken(String tokenName, LocalDate createdDate) {
        super(tokenName, createdDate);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
