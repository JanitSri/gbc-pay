package com.example.formvalidiation.controllers;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

        if(userService.userAlreadyExists(user.getEmail())){
            model.addAttribute("userExists", "The user already exists");
            return "register";
        }

        user.setEnabled(false);
        userService.registerNewUser(user);
        ra.addFlashAttribute("savedUser", user);
        return "redirect:/detail";
    }

    @GetMapping("/detail")
    public String getDetail(@ModelAttribute("user") User user){
        return "userDetail";
    }

    @GetMapping("/confirm")
    public String getConfirmMail(@RequestParam("token") String token, RedirectAttributes ra) {
        if(userService.enableUser(token)){
            ra.addFlashAttribute("validToken", "Token is Valid");
            return "redirect:/login";
        }
        ra.addFlashAttribute("inValidToken", "Token is not Valid");
        return "redirect:/login";
    }
}
