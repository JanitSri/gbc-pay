/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Controller to mange the routes for the registration and authentication of users.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.services.account.PasswordResetService;
import com.COMP3095.gbc_pay.services.account.RegisterService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public String getRegisterForm(@ModelAttribute("user") User user, @ModelAttribute("profile") Profile profile,
                                  @ModelAttribute("address") Address address) {
        return "account/register";
    }

    @PostMapping({"/register.html", "/register"})
    public String postRegisterForm(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                   @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                   @ModelAttribute("address") @Valid Address address, BindingResult addressResult,
                                   Model model, RedirectAttributes ra) {

        if (userResult.hasErrors() || profileResult.hasErrors() || addressResult.hasErrors()) {

            List<String> profileErrorMessages = profileResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            model.addAttribute("profileFormError", profileErrorMessages);

            List<String> userErrorMessages = userResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            model.addAttribute("userFormError", userErrorMessages);

            List<String> addressErrorMessages = addressResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            model.addAttribute("addressFormError", addressErrorMessages);

            return "account/register";
        }

        if (registerService.accountExists(profile.getEmail())) {
            model.addAttribute("infoForUser", "The user profile already exists.");
            return "account/register";
        }

        try {
            registerService.registerNewUser(user, profile, address);
            ra.addFlashAttribute("successfulRegister", "Registration successful. A verification link has " +
                    "been sent to " + profile.getEmail() + ".");
        } catch (MessagingException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Registration unsuccessful. Please try again.");
        }

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
    public String getForgotPassword(@ModelAttribute("profile") Profile profile) {
        return "account/forgot_password";
    }

    @PostMapping({"/forgot_password.html", "/forgot_password"})
    public String sendForgotPassword(@ModelAttribute("profile") @Valid Profile profile, BindingResult result, Model model,
                                     RedirectAttributes ra) {

        if (result.hasFieldErrors("email")) {
            return "account/forgot_password";
        }

        if (!registerService.accountExists(profile.getEmail())) {
            model.addAttribute("invalidAccount", "The account does not exists.");
            return "account/forgot_password";
        }

        try {
            passwordResetService.sendResetPasswordLink(profile);
            ra.addFlashAttribute("forgotPassword", "A password reset link has been sent to " +
                    profile.getEmail() + ".");
        } catch (MessagingException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Password reset unsuccessful. Please try again.");
        }

        return "redirect:/login";
    }

    @GetMapping({"/reset_password.html", "/reset_password"})
    public String getResetPassword(@RequestParam("token") String token, @ModelAttribute("profile") Profile profile, Model model,
                                   RedirectAttributes ra) {

        if (passwordResetService.invalidPasswordResetToken(token)) {
            ra.addFlashAttribute("error", "This password reset link is not valid.");
            return "redirect:/login";
        }

        model.addAttribute("emailToken", token);
        return "account/reset_password";
    }

    @PostMapping({"/reset_password.html", "/reset_password"})
    public String postResetPassword(@RequestParam("token") String token, @ModelAttribute("profile") @Valid Profile profile,
                                    BindingResult result, RedirectAttributes ra, Model model) {

        if (result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")) {
            model.addAttribute("emailToken", token);
            return "account/reset_password";
        }

        if (passwordResetService.resetPassword(profile, token)) {
            ra.addFlashAttribute("validToken", "Your password has been reset. Please login.");
        } else {
            ra.addFlashAttribute("error", "This password reset link is not valid.");
        }
        return "redirect:/login";
    }
}
