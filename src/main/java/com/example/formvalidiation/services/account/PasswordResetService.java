package com.example.formvalidiation.services.account;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.UserService;
import com.example.formvalidiation.services.email.EmailService;
import com.example.formvalidiation.services.email.PasswordResetEmail;
import com.example.formvalidiation.services.token.PasswordResetTokenService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final PasswordResetEmail passwordResetEmail;

    public PasswordResetService(UserService userService, PasswordResetTokenService passwordResetTokenService,
                                EmailService emailService, PasswordResetEmail passwordResetEmail) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.passwordResetEmail = passwordResetEmail;
    }

    public void sendResetPasswordLink(User existingUser) throws MessagingException {
        User user = userService.findByEmail(existingUser.getEmail());

        PasswordResetToken currentToken = user.getPasswordResetToken()
                .stream()
                .filter(passwordResetToken -> !passwordResetToken.isExpired())
                .findFirst()
                .orElse(null);

        if(currentToken == null) {
            currentToken = passwordResetTokenService.createToken(user);
            user.getPasswordResetToken().add(currentToken);
            userService.saveUser(user);
        }

        MimeMessage email = passwordResetEmail.constructMessage(user, currentToken);
        emailService.sendEmail(email);
    }

    private PasswordResetToken getPasswordResetToken(String resetToken){
        return passwordResetTokenService.validateToken(resetToken);
    }

    public boolean resetPassword(User existingUser, String resetToken){
        PasswordResetToken passwordResetToken = getPasswordResetToken(resetToken);
        if (passwordResetToken == null) return false;

        passwordResetToken.setExpired(true);
        User user = userService.findByEmail(passwordResetToken.getUser().getEmail());
        user.setPassword(existingUser.getPassword());
        userService.saveUser(user);

        return true;
    }

    public boolean validPasswordResetToken(String resetToken) {
        PasswordResetToken passwordResetToken = getPasswordResetToken(resetToken);
        if (passwordResetToken == null) return false;
        return true;
    }
}
