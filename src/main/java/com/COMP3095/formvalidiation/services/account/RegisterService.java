/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Service class to handle the user registration feature.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.account;

import com.COMP3095.formvalidiation.models.Role;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.models.VerificationToken;
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
        return userService.userExists(email);
    }

    public User registerNewUser(User newUser) throws MessagingException {
        newUser.setEmailVerified(false);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.saveUser(newUser);

        VerificationToken verificationToken = verificationTokenService.createToken(newUser);

        Role userRole = roleService.getRole("USER");
        newUser.getRoles().add(userRole);
        userRole.getUsers().add(newUser);
        userService.saveUser(newUser);

        newUser.setVerificationToken(verificationToken);
        userService.saveUser(newUser);

        MimeMessage email = this.email.constructMessage(newUser, verificationToken);

        emailService.sendEmail(email);

        return newUser;
    }

    public boolean enableUser(String userToken) {
        VerificationToken verificationToken = verificationTokenService.validateToken(userToken);
        if (verificationToken == null) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userService.saveUser(user);

        return true;
    }
}
