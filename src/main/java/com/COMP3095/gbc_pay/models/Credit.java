package com.COMP3095.gbc_pay.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @NotBlank(message = "City cannot be empty")
    private LocalDate expirationDate;

    @Pattern(regexp = "^[A-Za-z]+$", message = "Card Holder Name can only contain alphabets")
    private String cardHolderName;

    @NotBlank(message = "Card Number cannot be empty")
    private String cardHolderNumber;

    private boolean defaultCard;

    @ManyToOne
    private Profile profile;

    public Credit() {
    }

    public Credit(CardType cardType, LocalDate expirationDate, String cardHolderName, String cardHolderNumber, boolean defaultCard) {
        this.cardType = cardType;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
        this.cardHolderNumber = cardHolderNumber;
        this.defaultCard = defaultCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardHolderNumber() {
        return cardHolderNumber;
    }

    public void setCardHolderNumber(String cardHolderNumber) {
        this.cardHolderNumber = cardHolderNumber;
    }

    public boolean isDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(boolean defaultCard) {
        this.defaultCard = defaultCard;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
