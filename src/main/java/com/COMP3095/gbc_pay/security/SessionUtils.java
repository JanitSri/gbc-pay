/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Class that manages the session registry and ends the sessions of the user that is deleted from the system.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.security;

import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;


@Component
public class SessionUtils {

    private final SessionRegistry sessionRegistry;

    public SessionUtils(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void expireUserSession(Long profileId){
        for (Object principal: sessionRegistry.getAllPrincipals()) {
            UserDetailsImp userDetailsImp = (UserDetailsImp) principal;
            if(userDetailsImp.getProfile().getId().equals(profileId)){
                System.out.println("ENDING SESSION FOR USER WITH ID " + profileId);
                for (SessionInformation information : sessionRegistry.getAllSessions(userDetailsImp, true)) {
                    information.expireNow();
                }
            }
        }
    }
}
