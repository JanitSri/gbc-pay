package com.example.formvalidiation.controllers;

import com.example.formvalidiation.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class ViewController {

    @GetMapping("/detail")
    public String getDetail(@ModelAttribute("user") User user){
        return "userDetail";
    }
}
