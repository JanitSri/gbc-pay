package com.example.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String tokenName;

    public LocalDate createdDate;

    @OneToOne(mappedBy = "verificationToken")
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(String tokenName, LocalDate createdDate) {
        this.tokenName = tokenName;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String verifiedToken) {
        this.tokenName = verifiedToken;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", verifiedToken='" + tokenName + '\'' +
                ", createdDate=" + createdDate +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VerificationToken verificationToken = (VerificationToken) o;

        return id != null ? id.equals(verificationToken.id) : verificationToken.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
