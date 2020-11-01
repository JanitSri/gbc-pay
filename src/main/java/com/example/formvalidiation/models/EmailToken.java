package com.example.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class EmailToken extends Token{

    private boolean expired;

    @OneToOne(mappedBy = "emailToken")
    private User user;

    public EmailToken() {
    }

    public EmailToken(String tokenName, LocalDate createdDate, boolean expired) {
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

    @Override
    public String toString() {
        return "EmailToken{" +
                "expired=" + expired +
                ", user=" + user +
                '}';
    }
}
