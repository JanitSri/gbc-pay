package com.example.formvalidiation.services;

import com.example.formvalidiation.models.EmailToken;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.models.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MessageService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public MessageService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MimeMessage constructVerificationMessage(User user, VerificationToken verificationToken) throws MessagingException {
        final String message = String.format("<h1>Thank you for registering!</h1>" +
                "  <p>Name: %s %s</p>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:8080/confirm?token=%s\"><strong>" +
                "here</strong></a> to verify your account </p>", user.getFirstName(), user.getLastName(), user.getEmail(), verificationToken.getTokenName());

        String verificationHeader = "Financial Dashboard Confirmation Link!";

        return getMimeMessage(user, message, verificationHeader);
    }

    public MimeMessage constructPasswordResetMessage(User user, EmailToken emailToken) throws MessagingException {
        final String message = String.format("<h1>Password Reset</h1>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:8080/reset_password?token=%s\"><strong>" +
                "here</strong></a> to reset you password. </p>", user.getEmail(), emailToken.getTokenName());

        String emailHeader = "Financial Dashboard Password Reset";

        return getMimeMessage(user, message, emailHeader);
    }

    private MimeMessage getMimeMessage(User user, String message, String header) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        helper.setText(message,true);
        helper.setTo(user.getEmail());
        helper.setSubject(header);
        helper.setFrom(fromEmail);
        return mailMessage;
    }
}
