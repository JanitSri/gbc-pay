package com.example.formvalidiation.services.account;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.UserService;
import com.example.formvalidiation.services.email.Email;
import com.example.formvalidiation.services.email.EmailService;
import com.example.formvalidiation.services.token.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final Email email;

    public PasswordResetService(UserService userService, PasswordResetTokenService passwordResetTokenService,
                                EmailService emailService, @Qualifier("passwordResetEmail") Email email) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.email = email;
    }

    private PasswordResetToken getPasswordResetTokenByToken(String resetToken){
        return passwordResetTokenService.validateToken(resetToken);
    }

    private PasswordResetToken getPasswordResetTokenByUser(User user) {
        return passwordResetTokenService.getByUser(user);
    }

    public boolean validPasswordResetToken(String resetToken) {
        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);
        if (passwordResetToken == null || passwordResetToken.isExpired()) return false;
        return true;
    }

    public void sendResetPasswordLink(User existingUser) throws MessagingException {
        User user = userService.findByEmail(existingUser.getEmail());

        PasswordResetToken currentToken = getPasswordResetTokenByUser(user);
        if(currentToken == null) {
            currentToken = passwordResetTokenService.createToken(user);
            user.getPasswordResetToken().add(currentToken);
            userService.saveUser(user);
        }

        MimeMessage email = this.email.constructMessage(user, currentToken);
        emailService.sendEmail(email);
    }

    public boolean resetPassword(User existingUser, String resetToken){

        if (!validPasswordResetToken(resetToken)) return false;

        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);

        passwordResetToken.setExpired(true);
        User user = userService.findByEmail(passwordResetToken.getUser().getEmail());
        user.setPassword(existingUser.getPassword());
        userService.saveUser(user);

        return true;
    }
}
