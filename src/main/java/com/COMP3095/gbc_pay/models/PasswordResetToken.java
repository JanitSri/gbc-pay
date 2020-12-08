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
