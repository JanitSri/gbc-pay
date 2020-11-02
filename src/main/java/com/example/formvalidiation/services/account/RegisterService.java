package com.example.formvalidiation.services.account;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.models.VerificationToken;
import com.example.formvalidiation.services.UserService;
import com.example.formvalidiation.services.email.EmailService;
import com.example.formvalidiation.services.email.VerificationEmail;
import com.example.formvalidiation.services.token.VerificationTokenService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class RegisterService {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final VerificationEmail verificationEmail;
    private final EmailService emailService;

    public RegisterService(UserService userService, VerificationTokenService verificationTokenService,
                           VerificationEmail verificationEmail, EmailService emailService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.verificationEmail = verificationEmail;
        this.emailService = emailService;
    }

    public boolean accountExists(String email){
        return userService.userExists(email);
    }

    public void registerNewUser(User newUser) throws MessagingException {

        userService.saveUser(newUser);
        VerificationToken verificationToken = verificationTokenService.createToken(newUser);

        newUser.setVerificationToken(verificationToken);
        userService.saveUser(newUser);

        MimeMessage email = verificationEmail.constructMessage(newUser, verificationToken);

        emailService.sendEmail(email);
    }

    public boolean enableUser(String userToken){
        VerificationToken verificationToken = verificationTokenService.validateToken(userToken);
        if(verificationToken == null){
            return false;
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.saveUser(user);

        return true;
    }
}
