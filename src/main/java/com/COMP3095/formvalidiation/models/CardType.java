package com.COMP3095.formvalidiation.models;

public enum CardType {
    MASTER_CARD("Master Card"),
    VISA("Visa"),
    AMERICAN_EXPRESS("American Express");

    private final String cardName;

    CardType(String card) {
        this.cardName = card;
    }

    public String getCardName(){
        return cardName;
    }
}
