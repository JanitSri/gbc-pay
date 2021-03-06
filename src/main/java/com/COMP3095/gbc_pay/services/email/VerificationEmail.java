package com.COMP3095.gbc_pay.services.email;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.models.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("verificationEmail")
public class VerificationEmail implements Email<VerificationToken> {

    private final EmailService emailService;

    @Value("${server.port}")
    private int portNumber;

    public VerificationEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public MimeMessage constructMessage(User user, Profile profile, VerificationToken token) throws MessagingException {
        final String message = String.format("<h1>GBC PAY - Thank you for registering!</h1>" +
                        "  <p>Name: %s %s</p>" +
                        "  <p>Email: %s</p>" +
                        "  <p>Please click <a href=\"http://localhost:%d/confirm?token=%s\"><strong>" +
                        "here</strong></a> to verify your account </p>", user.getFirstName(), user.getLastName(),
                profile.getEmail(), portNumber, token.getTokenName());

        String verificationHeader = "Financial Dashboard Confirmation Link!";

        return emailService.createEmail(profile, message, verificationHeader);
    }
}
