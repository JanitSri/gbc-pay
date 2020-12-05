/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Service class for the password reset feature. Sending and verifying the token.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.services.account;

import com.COMP3095.gbc_pay.models.PasswordResetToken;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.services.user.UserService;
import com.COMP3095.gbc_pay.services.email.Email;
import com.COMP3095.gbc_pay.services.email.EmailService;
import com.COMP3095.gbc_pay.services.token.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;
    private final Email email;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserService userService, PasswordResetTokenService passwordResetTokenService,
                                EmailService emailService, @Qualifier("passwordResetEmail") Email email,
                                PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.email = email;
        this.passwordEncoder = passwordEncoder;
    }

    private PasswordResetToken getPasswordResetTokenByToken(String resetToken) {
        return passwordResetTokenService.validateToken(resetToken);
    }

    private PasswordResetToken getPasswordResetTokenByProfile(Profile profile) {
        return passwordResetTokenService.getByProfile(profile);
    }

    public boolean invalidPasswordResetToken(String resetToken) {
        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);
        return passwordResetToken == null || passwordResetToken.isExpired();
    }

    public void sendResetPasswordLink(Profile existingProfile) throws MessagingException {
        Profile profile = userService.findByEmail(existingProfile.getEmail());

        PasswordResetToken currentToken = getPasswordResetTokenByProfile(profile);
        if (currentToken == null) {
            currentToken = passwordResetTokenService.createToken(profile);
            profile.getPasswordResetTokens().add(currentToken);
            userService.saveProfile(profile);
        }

        MimeMessage email = this.email.constructMessage(profile.getUser(), profile, currentToken);
        emailService.sendEmail(email);
    }

    public boolean resetPassword(Profile existingProfile, String resetToken) {

        if (invalidPasswordResetToken(resetToken)) return false;

        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);

        passwordResetToken.setExpired(true);
        Profile profile = userService.findByEmail(passwordResetToken.getProfile().getEmail());
        profile.setPassword(passwordEncoder.encode(existingProfile.getPassword()));
        userService.saveProfile(profile);

        return true;
    }
}
