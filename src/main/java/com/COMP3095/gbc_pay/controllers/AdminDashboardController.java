package com.COMP3095.gbc_pay.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/dashboard/admin", "/dashboard.html/admin"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminDashboardController {

    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(){
        return "dashboard/index";
    }

    @GetMapping({"my_profile", "my_profile.html"})
    public String getAdminMyProfilePage(){
        return "dashboard/my_profile";
    }

    @GetMapping({"users", "users.html"})
    public String getAdminUsersPage(){
        return "dashboard/users";
    }

    @GetMapping({"inbox", "inbox.html"})
    public String getAdminInboxPage(){
        return "dashboard/inbox";
    }

    @GetMapping({"support", "support.html"})
    public String getAdminSupportPage(){
        return "dashboard/support";
    }
}
