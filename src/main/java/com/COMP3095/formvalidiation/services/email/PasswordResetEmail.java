/********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Email message handler for sending email for password reset.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.email;

import com.COMP3095.formvalidiation.models.PasswordResetToken;
import com.COMP3095.formvalidiation.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("passwordResetEmail")
public class PasswordResetEmail implements Email<PasswordResetToken> {

    private final EmailService emailService;

    @Value("${server.port}")
    private int portNumber;

    public PasswordResetEmail(EmailService emailService) {
        this.emailService = emailService;
    }

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
