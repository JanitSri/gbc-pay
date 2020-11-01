package com.example.formvalidiation.services;

import com.example.formvalidiation.models.EmailToken;
import com.example.formvalidiation.models.VerificationToken;
import com.example.formvalidiation.models.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class AccountService {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailTokenService emailTokenService;
    private final EmailService emailService;
    private final MessageService messageService;


    public AccountService(UserService userService, VerificationTokenService verificationTokenService,
                          EmailTokenService emailTokenService, EmailService emailService, MessageService messageService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.emailTokenService = emailTokenService;
        this.emailService = emailService;
        this.messageService = messageService;
    }

    public boolean accountExists(String email){
        return userService.userExists(email);
    }

    public void registerNewUser(User newUser) throws MessagingException {

        userService.saveUser(newUser);
        VerificationToken verificationToken = verificationTokenService.createToken(newUser);

        newUser.setVerificationToken(verificationToken);
        userService.saveUser(newUser);

        emailService.sendEmail(messageService.constructVerificationMessage(newUser, verificationToken));

    }

    public boolean enableUser(String userToken){
        VerificationToken verificationToken = verificationTokenService.validateToken(userToken);
        if(verificationToken == null){
            return false;
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        user.setConfirmPassword(user.getPassword());
        user.setAgreedToTerms(true);
        userService.saveUser(user);

        return true;
    }

    public void sendResetPasswordLink(User existingUser) throws MessagingException {
        User user = userService.findByEmail(existingUser.getEmail());

        EmailToken emailToken = emailTokenService.createToken(user);

        user.setEmailToken(emailToken);
        user.setConfirmPassword(user.getPassword());
        user.setAgreedToTerms(true);
        userService.saveUser(user);
        emailService.sendEmail(messageService.constructPasswordResetMessage(user, emailToken));
    }

    public boolean resetPassword(User existingUser, String resetToken){
        EmailToken emailToken = emailTokenService.validateToken(resetToken);
        if(emailToken == null){
            return false;
        }

        User user = userService.findByEmail(emailToken.getUser().getEmail());
        user.setPassword(existingUser.getPassword());
        user.setConfirmPassword(existingUser.getConfirmPassword());
        user.setAgreedToTerms(true);
        userService.saveUser(user);

        return true;
    }
}










