/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Service class for the credit. CRUD operations for credit.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.services.dashboard.user;

import com.COMP3095.gbc_pay.models.Credit;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.stereotype.Service;

import java.net.CacheRequest;

@Service
public class CreditProfileService {

    private final UserService userService;

    public CreditProfileService(UserService userService) {
        this.userService = userService;
    }

    public Credit getCreditById(Profile profile, String cardIdNumber){
        return profile.getCredits()
                .stream()
                .filter(credit1 -> credit1.getId() == Integer.parseInt(cardIdNumber))
                .findFirst()
                .get();
    }

    public Credit getCreditByCardNumber(Profile profile, Credit card){
        return profile.getCredits()
                .stream()
                .filter(credit1 -> credit1.getCardHolderNumber().equals(card.getCardHolderNumber()))
                .findFirst()
                .orElse(null);
    }

    public Credit getDefaultCard(Profile profile){
        return profile.getCredits()
                .stream()
                .filter(Credit::isDefaultCard)
                .findFirst()
                .orElse(null);
    }

    public void AddOrUpdateCard(Profile currentProfile, Credit card){

        Credit currentDefaultCard = getDefaultCard(currentProfile);
        if(currentDefaultCard != null && card.isDefaultCard() &&
                !currentDefaultCard.getCardHolderNumber().equals(card.getCardHolderNumber())
        ){
            System.out.println("CHANGING DEFAULT CARD");
            currentDefaultCard.setDefaultCard(false);
        }

        Credit existingCard = getCreditByCardNumber(currentProfile, card);

        if(existingCard == null){
            System.out.println("ADDING NEW CARD");
            currentProfile.getCredits().add(card);
            card.setProfile(currentProfile);
        }else{
            System.out.println("UPDATING CARD");
            for(Credit c: currentProfile.getCredits()){
                if(c.getCardHolderNumber().equals(card.getCardHolderNumber())){
                    c.setCardType(card.getCardType());
                    c.setExpirationDate(card.getExpirationDate());
                    c.setCardHolderName(card.getCardHolderName());
                    c.setCardHolderNumber(card.getCardHolderNumber());
                    c.setDefaultCard(card.isDefaultCard());
                }
            }
        }

        userService.saveProfile(currentProfile);
    }


    public void deleteCard(Profile profile, Credit card){
        if(getCreditByCardNumber(profile, card) != null){
            Credit existingCard = getCreditByCardNumber(profile, card);
            existingCard.setProfile(null);
            userService.saveCard(existingCard);
            userService.deleteCard(existingCard);

            profile.getCredits()
                    .removeIf(credit -> credit.getCardHolderNumber().equals(card.getCardHolderNumber()));
        }
        userService.saveProfile(profile);
    }
}