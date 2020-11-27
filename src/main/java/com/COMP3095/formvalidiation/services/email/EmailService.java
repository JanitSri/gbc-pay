/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Service class to send emails to the user.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.email;

import com.COMP3095.formvalidiation.models.Profile;
import com.COMP3095.formvalidiation.models.User;
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

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(MimeMessage message) {
        javaMailSender.send(message);
    }

    public MimeMessage createEmail(Profile profile, String message, String header) throws MessagingException {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        helper.setText(message, true);
        helper.setTo(profile.getEmail());
        helper.setSubject(header);
        helper.setFrom(fromEmail);
        return mailMessage;
    }
}