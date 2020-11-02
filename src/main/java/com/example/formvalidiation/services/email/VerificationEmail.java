package com.example.formvalidiation.services.email;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.models.VerificationToken;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class VerificationEmail extends Email<VerificationToken> {

    private final EmailService emailService;

    public VerificationEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public MimeMessage constructMessage(User user, VerificationToken token) throws MessagingException {
        final String message = String.format("<h1>Thank you for registering!</h1>" +
                "  <p>Name: %s %s</p>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:8080/confirm?token=%s\"><strong>" +
                "here</strong></a> to verify your account </p>", user.getFirstName(), user.getLastName(),
                user.getEmail(), token.getTokenName());

        String verificationHeader = "Financial Dashboard Confirmation Link!";

        return emailService.createEmail(user, message, verificationHeader);
    }
}
