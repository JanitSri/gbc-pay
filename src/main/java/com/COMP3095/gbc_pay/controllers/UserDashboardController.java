package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.services.dashboard.DashboardUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping({"/dashboard", "/dashboard.html"})
public class UserDashboardController {

    private final DashboardUserService dashboardUserService;

    public UserDashboardController(DashboardUserService dashboardUserService) {
        this.dashboardUserService = dashboardUserService;
    }

    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(Model model){
        model.addAttribute("currentProfile", dashboardUserService.getAuthenticatedProfile());

        model.addAttribute("lastLogin",
                dashboardUserService.getLastLogin().format(DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy")));
        model.addAttribute("lastUpdate",
                dashboardUserService.getAuthenticatedProfile().getUser().getLastUpdate()
                        .format(DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy")));

        model.addAttribute("defaultBillingAddress", dashboardUserService.getBillingAddress());
        model.addAttribute("defaultShippingAddress", dashboardUserService.getShippingAddress());

        return "dashboard/user/index";
    }

    @GetMapping({"my_profile", "my_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getMyProfilePage(HttpServletRequest request, Model model, @ModelAttribute("updateEmail") String updateEmail){

        String email = updateEmail.isEmpty() ? request.getParameter("email") : updateEmail;

        Profile profile = dashboardUserService.getAuthenticatedProfile();

        if(email != null){
            profile = dashboardUserService.getProfileByEmail(email);
        }

        System.out.println("The view email is " + email);

        model.addAttribute("currentProfile", profile);
        model.addAttribute("allProfiles", dashboardUserService.getAllProfiles());

        return "dashboard/user/my_profile";
    }

    @PostMapping({"my_profile/delete", "my_profile.html/delete"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteProfile(HttpServletRequest request, RedirectAttributes ra){

        String deleteEmail = request.getParameter("delete-email");

        System.out.println("The delete email is " + deleteEmail);

        ra.addFlashAttribute("deletedEmail", "Profile with the email '" + deleteEmail + "' has been deleted.");

        return "redirect:/dashboard/my_profile";
    }

    @PostMapping({"my_profile/update", "my_profile.html/update"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateProfile(HttpServletRequest request, RedirectAttributes ra){

        String updateEmail = request.getParameter("update-email");

        System.out.println("The update email is " + updateEmail);

        ra.addFlashAttribute("updatedProfile", "Profile with the email '" + updateEmail + "' has been updated.");
        ra.addAttribute("updateEmail", updateEmail);

        return "redirect:/dashboard/my_profile";
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
