package com.COMP3095.formvalidiation.services.account;

import com.COMP3095.formvalidiation.models.PasswordResetToken;
import com.COMP3095.formvalidiation.models.Profile;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.services.user.UserService;
import com.COMP3095.formvalidiation.services.email.Email;
import com.COMP3095.formvalidiation.services.email.EmailService;
import com.COMP3095.formvalidiation.services.token.PasswordResetTokenService;
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

    private PasswordResetToken getPasswordResetTokenByUser(Profile profile) {
        return passwordResetTokenService.getByProfile(profile);
    }

    public boolean validPasswordResetToken(String resetToken) {
        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);
        return passwordResetToken != null && !passwordResetToken.isExpired();
    }

    public void sendResetPasswordLink(Profile existingProfile) throws MessagingException {
        Profile profile = userService.findByEmail(existingProfile.getEmail());

        PasswordResetToken currentToken = getPasswordResetTokenByUser(profile);
        if (currentToken == null) {
            currentToken = passwordResetTokenService.createToken(profile);
            profile.getPasswordResetTokens().add(currentToken);
            userService.saveProfile(profile);
        }

        MimeMessage email = this.email.constructMessage(profile.getUser(), profile, currentToken);
        emailService.sendEmail(email);
    }

    public boolean resetPassword(Profile existingProfile, String resetToken) {

        if (!validPasswordResetToken(resetToken)) return false;

        PasswordResetToken passwordResetToken = getPasswordResetTokenByToken(resetToken);

        passwordResetToken.setExpired(true);
        Profile profile = userService.findByEmail(passwordResetToken.getProfile().getEmail());
        profile.setPassword(passwordEncoder.encode(existingProfile.getPassword()));
        userService.saveProfile(profile);

        return true;
    }
}
