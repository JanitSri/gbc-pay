package com.COMP3095.gbc_pay.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping({"/dashboard", "/dashboard.html"})
public class UserDashboardController {

    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(){
        return "dashboard/index";
    }


    @GetMapping({"my_profile", "my_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getMyProfilePage(){
        return "dashboard/my_profile";
    }

    @GetMapping({"credit_profile", "credit_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCreditProfilePage(){
        return "dashboard/credit_profile";
    }

    @GetMapping({"inbox", "inbox.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getInboxPage(){
        return "dashboard/inbox";
    }

    @GetMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getSupportPage(){
        return "dashboard/support";
    }
}
