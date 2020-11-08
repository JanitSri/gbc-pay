/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: The controller for all endpoints dealing with account management (login, register, forget password,
 *               reset password)
 *********************************************************************************/

package com.example.formvalidiation.controllers;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.account.PasswordResetService;
import com.example.formvalidiation.services.account.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AccountController {

    private final RegisterService registerService;
    private final PasswordResetService passwordResetService;

    public AccountController(RegisterService registerService, PasswordResetService passwordResetService) {

        this.registerService = registerService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping({"/", "/login.html", "/login"})
    public String getLogin() {
        return "account/login";
    }

    @PostMapping("/fail_login")
    public String handleLoginError(Model model, @RequestBody MultiValueMap<String, String> formData,
                                   RedirectAttributes ra) {

        String password = formData.get("password").get(0);
        String username = formData.get("username").get(0);

        if (!password.isEmpty() || !username.isEmpty()) {
            ra.addFlashAttribute("error", "Invalid username and/or password");
        }
        return "redirect:/login";
    }

    @GetMapping("/denied_login")
    public String handleAccessDenied(RedirectAttributes ra) {
        ra.addFlashAttribute("error", "You have to login to access that page.");
        return "redirect:/login";
    }

    @GetMapping({"/logout.html", "/logout"})
    public String getLogout(HttpSession session, SessionStatus status) {
        status.setComplete();
        session.invalidate();
        return "account/login";
    }

    @GetMapping({"/register.html", "/register"})
    public String getRegisterForm(@ModelAttribute("user") User user) {
        return "account/register";
    }

    @PostMapping({"/register.html", "/register"})
    public String postRegisterForm(@ModelAttribute("user") @Valid User user, BindingResult result, Model model,
                                   RedirectAttributes ra, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "account/register";
        }

        if (registerService.accountExists(user.getEmail())) {
            model.addAttribute("infoForUser", "The user already exists.");
            return "account/register";
        }

        try {
            String initialPassword = user.getPassword();
            registerService.registerNewUser(user);
            ra.addFlashAttribute("successfulRegister", "Registration successful. A verification link has " +
                    "been sent to " + user.getEmail() + ".");
            request.login(user.getEmail(), initialPassword);
            return "redirect:/dashboard";
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        ra.addFlashAttribute("error", "Registration unsuccessful. Please try again.");
        return "redirect:/login";
    }

    @GetMapping({"/confirm.html", "/confirm"})
    public String getConfirmMail(@RequestParam("token") String token, RedirectAttributes ra) {
        if (registerService.enableUser(token)) {
            ra.addFlashAttribute("validToken", "Thank you for verifying your email.");
        } else {
            ra.addFlashAttribute("error", "This verification link is not valid.");
        }
        return "redirect:/login";
    }

    @GetMapping({"/forgot_password.html", "/forgot_password"})
    public String getForgotPassword(@ModelAttribute("user") User user) {
        return "account/forgot_password";
    }

    @PostMapping({"/forgot_password.html", "/forgot_password"})
    public String sendForgotPassword(@ModelAttribute("user") @Valid User user, BindingResult result, Model model,
                                     RedirectAttributes ra) {

        if (result.hasFieldErrors("email")) {
            return "account/forgot_password";
        }

        if (!registerService.accountExists(user.getEmail())) {
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

        if (!passwordResetService.validPasswordResetToken(token)) {
            ra.addFlashAttribute("error", "This password reset link is not valid.");
            return "redirect:/login";
        }

        model.addAttribute("emailToken", token);
        return "account/reset_password";
    }

    @PostMapping({"/reset_password.html", "/reset_password"})
    public String postResetPassword(@RequestParam("token") String token, @ModelAttribute("user") @Valid User user,
                                    BindingResult result, RedirectAttributes ra, Model model) {

        if (result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")) {
            model.addAttribute("emailToken", token);
            return "account/reset_password";
        }

        if (passwordResetService.resetPassword(user, token)) {
            ra.addFlashAttribute("validToken", "Your password has been reset. Please login.");
        } else {
            ra.addFlashAttribute("error", "This password reset link is not valid.");
        }
        return "redirect:/login";
    }
}
