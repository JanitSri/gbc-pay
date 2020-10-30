package com.example.formvalidiation.services;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender, JavaMailSender mailSender) {
        this.javaMailSender = javaMailSender;
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(User user, Token token) throws MessagingException {
        MimeMessage mailMessage = constructMessage(user, token);

        javaMailSender.send(mailMessage);
    }

    private MimeMessage constructMessage(User user, Token token) throws MessagingException {
        final String message = String.format("<h1>Thank you for registering!</h1>" +
                "  <p>Name: %s %s</p>" +
                "  <p>Email: %s</p>" +
                "  <p>Please click <a href=\"http://localhost:8080/confirm?token=%s\"><strong>" +
                "here</strong></a> to verify your account </p>", user.getFirstName(), user.getLastName(), user.getEmail(), token.getTokenName());


        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        helper.setText(message,true);
        helper.setTo(user.getEmail());
        helper.setSubject("Financial Dashboard Confirmation Link!");
        helper.setFrom(fromEmail);
        return mailMessage;
    }
}