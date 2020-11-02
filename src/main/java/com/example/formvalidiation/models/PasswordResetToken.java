package com.example.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PasswordResetToken extends Token{

    private boolean expired;

    @OneToOne(mappedBy = "passwordResetToken")
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
