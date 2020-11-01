package com.example.formvalidiation.controllers;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({"/", "/register"})
    public String getRegisterForm(@ModelAttribute("user") User user){
        return "register";
    }

    @GetMapping("/login")
    public String getLogin(@ModelAttribute("user") User user){
        return "login";
    }

    @PostMapping("/register")
    public String postRegisterForm(@ModelAttribute("user") @Valid User user, BindingResult result,
                                   RedirectAttributes ra, Model model){

        if(result.hasErrors()){
            return "register";
        }

        if(accountService.accountExists(user.getEmail())){
            model.addAttribute("userExists", "The user already exists");
            return "register";
        }

        try {
            accountService.registerNewUser(user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        user.setEnabled(false);
        ra.addFlashAttribute("savedUser", user);
        return "redirect:/detail";
    }

    @GetMapping("/confirm")
    public String getConfirmMail(@RequestParam("token") String token, RedirectAttributes ra) {
        if(accountService.enableUser(token)){
            ra.addFlashAttribute("validToken", "Thank you for validating your email.");
            return "redirect:/login";
        }
        ra.addFlashAttribute("inValidToken", "This validation link is not valid.");
        return "redirect:/login";
    }

    @GetMapping("/forgot_password")
    public String getForgotPassword(@ModelAttribute("user") User user){
        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String sendForgotPassword(@ModelAttribute("user") @Valid User user, BindingResult result, Model model){

        if(result.hasFieldErrors("email")){
            return "forgot_password";
        }

        if(!accountService.accountExists(user.getEmail())){
            model.addAttribute("invalidAccount", "The account does not exists");
            return "forgot_password";
        }

        try {
            accountService.sendResetPasswordLink(user);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "login";
    }

    @GetMapping("/reset_password")
    public String getResetPassword(@RequestParam("token") String token, @ModelAttribute("user") User user, Model model) {
        model.addAttribute("emailToken", token);
        return "reset_password";
    }

    @PostMapping("/reset_password")
    public String postResetPassword(@RequestParam("token") String token, @ModelAttribute("user") @Valid User user,
                                    BindingResult result, RedirectAttributes ra, Model model) {

        if(result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")){
            model.addAttribute("emailToken", token);
            return "reset_password";
        }

        if(accountService.resetPassword(user, token)){
            ra.addFlashAttribute("validToken", "Your password has been reset. Please login");
            return "redirect:/login";
        }
        ra.addFlashAttribute("inValidToken", "This password link is not valid.");
        return "redirect:/login";
    }
}
