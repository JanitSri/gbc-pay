package com.example.formvalidiation.services;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.TokenRepository;
import com.example.formvalidiation.repositories.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, TokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    public void registerNewUser(User newUser){

        userRepository.save(newUser);
        Token token = tokenService.createToken(newUser);

        newUser.setValidationToken(token);
        userRepository.save(newUser);

        sendVerificationEmail(newUser.getEmail(), token.getVerifiedToken());
    }

    public boolean enableUser(String userToken){
        Token token = tokenService.validateToken(userToken);
        if(token == null){
            return false;
        }

        User user = token.getUser();
        user.setEnabled(true);
        user.setConfirmPassword(user.getPassword());
        userRepository.save(user);
        return true;
    }

    private User findByEmail(String email){
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);

        if(user.isPresent()){
            return user.get();
        }

        return null;
    }

    public boolean userAlreadyExists(String email){
        return findByEmail(email) == null ? false : true;
    }

    private void sendVerificationEmail(String userEmail, String token){
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Financial Dashboard Confirmation Link!");
        mailMessage.setFrom("confirm@financialdashboard.com");
        mailMessage.setText(
                "Thank you for registering for the financial dashboard. Please click on the below link to activate your account." + "http://localhost:8080/confirm?token="
                        + token);

        emailService.sendEmail(mailMessage);
    }
}
