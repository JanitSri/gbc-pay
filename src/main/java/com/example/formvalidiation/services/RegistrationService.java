package com.example.formvalidiation.services;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class RegistrationService {
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    public RegistrationService(UserService userService, TokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    public void registerNewUser(User newUser){

        userService.saveUser(newUser);
        Token token = tokenService.createToken(newUser);

        newUser.setVerificationToken(token);
        userService.saveUser(newUser);

        try {
            emailService.sendEmail(newUser, token);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean enableUser(String userToken){
        Token token = tokenService.validateToken(userToken);
        if(token == null){
            return false;
        }

        User user = token.getUser();
        user.setEnabled(true);
        user.setConfirmPassword(user.getPassword());
        user.setAgreedToTerms(true);
        userService.saveUser(user);
        return true;
    }

}
