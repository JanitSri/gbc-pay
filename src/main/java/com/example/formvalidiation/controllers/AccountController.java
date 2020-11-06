package com.example.formvalidiation.controllers;

import com.example.formvalidiation.RecaptchaResponse;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.account.PasswordResetService;
import com.example.formvalidiation.services.account.RegisterService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {

    private final RegisterService registerService;
    private final PasswordResetService passwordResetService;
    private final RestTemplate restTemplate;

    public AccountController(RegisterService registerService, PasswordResetService passwordResetService,
                             RestTemplate restTemplate) {

        this.registerService = registerService;
        this.passwordResetService = passwordResetService;
        this.restTemplate = restTemplate;
    }

    @GetMapping({"/", "/login.html", "/login"})
    public String getLogin(){
        return "account/login";
    }

    @PostMapping({"/", "/login.html", "/login"})
    public String postLogin(HttpSession session, @RequestParam("g-recaptcha-response") String response,
                            Model model){
        String reCaptchaURL = "https://www.google.com/recaptcha/api/siteverify";
        String urlParams = "?secret=6LdWp98ZAAAAAO-rTDmyfz3Np71C4aZ9nigyysxy&response="+response;

        RecaptchaResponse recaptchaResponse = restTemplate.exchange(reCaptchaURL+urlParams, HttpMethod.POST,
                null, RecaptchaResponse.class).getBody();

        if(!recaptchaResponse.isSuccess()){
            model.addAttribute("error", "Are you a robot from the future? Please verify you are not.");
            return "account/login";
        }

        session.setAttribute("currentUser", "User");
        return "redirect:/dashboard";
    }

    @GetMapping({"/logout.html", "/logout"})
    public String getLogout(HttpSession session, SessionStatus status){
        status.setComplete();
        session.invalidate();
        return "account/login";
    }

    @GetMapping({"/register.html", "/register"})
    public String getRegisterForm(@ModelAttribute("user") User user){
        return "account/register";
    }

    @PostMapping({"/register.html", "/register"})
    public String postRegisterForm(@ModelAttribute("user") @Valid User user, BindingResult result, Model model,
            RedirectAttributes ra){

        if(result.hasErrors()){
            return "account/register";
        }

        if(registerService.accountExists(user.getEmail())){
            model.addAttribute("infoForUser", "The user already exists.");
            return "account/register";
        }

        try {
            registerService.registerNewUser(user);
            ra.addFlashAttribute("successfulRegister", "Registration successful. Please login.");
        } catch (MessagingException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Registration successful. Please login.");
        }

        return "redirect:/login";
    }

    @GetMapping({"/confirm.html", "/confirm"})
    public String getConfirmMail(@RequestParam("token") String token, RedirectAttributes ra) {
        if(registerService.enableUser(token)){
            ra.addFlashAttribute("validToken", "Thank you for verifying your email.");

        }else{
            ra.addFlashAttribute("error", "This verification link is not valid.");
        }
        return "redirect:/login";
    }

    @GetMapping({"/forgot_password.html", "/forgot_password"})
    public String getForgotPassword(@ModelAttribute("user") User user){
        return "account/forgot_password";
    }

    @PostMapping({"/forgot_password.html", "/forgot_password"})
    public String sendForgotPassword(@ModelAttribute("user") @Valid User user, BindingResult result, Model model,
                                     RedirectAttributes ra){

        if(result.hasFieldErrors("email")){
            return "account/forgot_password";
        }

        if(!registerService.accountExists(user.getEmail())){
            model.addAttribute("invalidAccount", "The account does not exists.");
            return "account/forgot_password";
        }

        try {
            passwordResetService.sendResetPasswordLink(user);
            ra.addFlashAttribute("forgotPassword", "A password reset link has been sent to " +
                    user.getEmail() + ".");
        } catch (MessagingException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Password reset unsuccessful. Please try again.");
        }

        return "redirect:/login";
    }

    @GetMapping({"/reset_password.html", "/reset_password"})
    public String getResetPassword(@RequestParam("token") String token, @ModelAttribute("user") User user, Model model,
                                   RedirectAttributes ra) {

        if(!passwordResetService.validPasswordResetToken(token)){
            ra.addFlashAttribute("error", "This password reset link is not valid.");
            return "redirect:/login";
        }

        model.addAttribute("emailToken", token);
        return "account/reset_password";
    }

    @PostMapping({"/reset_password.html", "/reset_password"})
    public String postResetPassword(@RequestParam("token") String token, @ModelAttribute("user") @Valid User user,
                                    BindingResult result, RedirectAttributes ra, Model model) {

        if(result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")){
            model.addAttribute("emailToken", token);
            return "account/reset_password";
        }

        if(passwordResetService.resetPassword(user, token)){
            ra.addFlashAttribute("validToken", "Your password has been reset. Please login.");
        }else{
            ra.addFlashAttribute("error", "This password reset link is not valid.");
        }
        return "redirect:/login";
    }
}
