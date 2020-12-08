package com.COMP3095.gbc_pay.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Pattern(regexp = "^(3[01]|[12][0-9]|0[1-9]) [0-9]{4}$", message = "Expiration date has to be in the format MM YYYY. e.x. 01 2020")
    private String expirationDate;

    @NotBlank(message = "Card Holder Name cannot be empty")
    private String cardHolderName;

    @NotBlank(message = "Card Number cannot be empty")
    private String cardHolderNumber;

    private boolean defaultCard;

    @ManyToOne
    private Profile profile;

    public Credit() {
    }

    public Credit(CardType cardType, String expirationDate, String cardHolderName, String cardHolderNumber, boolean defaultCard) {
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credit credit = (Credit) o;

        return id != null ? id.equals(credit.id) : credit.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
