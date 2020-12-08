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
