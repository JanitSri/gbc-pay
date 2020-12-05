/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Enum representing the types of credit allowed in the application.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.models;

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
