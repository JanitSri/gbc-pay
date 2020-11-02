package com.example.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class VerificationToken extends Token{

    @OneToOne(mappedBy = "verificationToken")
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
