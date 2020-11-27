package com.COMP3095.formvalidiation.services.account;

import com.COMP3095.formvalidiation.models.*;
import com.COMP3095.formvalidiation.services.user.RoleService;
import com.COMP3095.formvalidiation.services.user.UserService;
import com.COMP3095.formvalidiation.services.email.Email;
import com.COMP3095.formvalidiation.services.email.EmailService;
import com.COMP3095.formvalidiation.services.token.VerificationTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;

@Service
public class RegisterService {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final Email email;
    private final EmailService emailService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserService userService, VerificationTokenService verificationTokenService,
                           @Qualifier("verificationEmail") Email email, EmailService emailService, RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.email = email;
        this.emailService = emailService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean accountExists(String email) {
        return userService.userProfileExists(email);
    }

    public void registerNewUser(User newUser, Profile newProfile, Address newAddress) throws MessagingException {
        newProfile.setEmailVerified(false);
        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));
        newProfile.setLastLogin(LocalDate.now());

        newProfile.setAddress(newAddress);

        Role userRole = roleService.getRole("USER");
        newProfile.getRoles().add(userRole);
        userRole.getProfiles().add(newProfile);

        VerificationToken verificationToken = verificationTokenService.createToken(newProfile);
        newProfile.setVerificationToken(verificationToken);

        newProfile.setUser(newUser);
        newUser.getProfiles().add(newProfile);

        userService.saveUser(newUser);

        MimeMessage email = this.email.constructMessage(newUser, newProfile, verificationToken);

        emailService.sendEmail(email);
    }

    public boolean enableUser(String userToken) {
        VerificationToken verificationToken = verificationTokenService.validateToken(userToken);
        if (verificationToken == null) {
            return false;
        }

        Profile profile = verificationToken.getProfile();
        profile.setEmailVerified(true);
        userService.saveProfile(profile);

        return true;
    }
}
