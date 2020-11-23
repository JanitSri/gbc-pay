package com.COMP3095.formvalidiation.models;

import java.time.LocalDate;

public class Credit {

    private Long id;
    private CardType cardType;
    private String cardHolderName;
    private String cardHolderNumber;
    private LocalDate expirationDate;
    private boolean defaultCard;
}
