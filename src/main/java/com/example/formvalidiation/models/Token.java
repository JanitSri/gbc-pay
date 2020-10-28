package com.example.formvalidiation.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verifiedToken;

    private LocalDate createdDate;

    @OneToOne(mappedBy = "validationToken")
    private User user;

    public Token() {
    }

    public Token(String verifiedToken, LocalDate createdDate) {
        this.verifiedToken = verifiedToken;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerifiedToken() {
        return verifiedToken;
    }

    public void setVerifiedToken(String verifiedToken) {
        this.verifiedToken = verifiedToken;
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
                ", verifiedToken='" + verifiedToken + '\'' +
                ", createdDate=" + createdDate +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return id != null ? id.equals(token.id) : token.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
