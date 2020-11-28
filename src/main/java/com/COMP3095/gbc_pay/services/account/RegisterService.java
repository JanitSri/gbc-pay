package com.COMP3095.gbc_pay.services.account;

import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.services.user.RoleService;
import com.COMP3095.gbc_pay.services.user.UserService;
import com.COMP3095.gbc_pay.services.email.Email;
import com.COMP3095.gbc_pay.services.email.EmailService;
import com.COMP3095.gbc_pay.services.token.VerificationTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

        User existingUser = userService.findByUser(newUser);

        newProfile.setEmailVerified(false);
        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));

        newProfile.setAddress(newAddress);
        newAddress.setProfile(newProfile);

        Role userRole = roleService.getRole("USER");
        newProfile.setRole(userRole);
        userRole.setProfile(newProfile);

        VerificationToken verificationToken = verificationTokenService.createToken(newProfile);
        newProfile.setVerificationToken(verificationToken);

        if(existingUser != null){
            newUser = existingUser;
        }

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
