package com.example.formvalidiation.services.email;

import com.example.formvalidiation.models.PasswordResetToken;
import com.example.formvalidiation.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("passwordResetEmail")
public class PasswordResetEmail implements Email<PasswordResetToken> {

    private final EmailService emailService;

    @Value("${server.port}")
    private int portNumber;

    public PasswordResetEmail(EmailService emailService) { this.emailService = emailService; }

    @Override
    public MimeMessage constructMessage(User user, PasswordResetToken token) throws MessagingException {
        final String message = String.format("<h1>GBC PAY - Password Reset</h1>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:%d/reset_password?token=%s\"><strong>" +
                "here</strong></a> to reset you password. </p>", user.getEmail(), portNumber, token.getTokenName());

        String emailHeader = "Financial Dashboard Password Reset";

        return emailService.createEmail(user, message, emailHeader);
    }
}
