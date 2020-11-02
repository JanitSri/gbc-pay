package com.example.formvalidiation.services.email;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetEmail extends Email<PasswordResetToken> {

    private final EmailService emailService;

    public PasswordResetEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public MimeMessage constructMessage(User user, PasswordResetToken token) throws MessagingException {
        final String message = String.format("<h1>Password Reset</h1>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:8080/reset_password?token=%s\"><strong>" +
                "here</strong></a> to reset you password. </p>", user.getEmail(), token.getTokenName());

        String emailHeader = "Financial Dashboard Password Reset";

        return emailService.createEmail(user, message, emailHeader);
    }
}
