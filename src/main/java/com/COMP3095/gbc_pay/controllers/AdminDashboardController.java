/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Controller to mange the routes for the admin users.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Message;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.services.dashboard.MessageService;
import com.COMP3095.gbc_pay.services.dashboard.admin.AdminProfileService;
import com.COMP3095.gbc_pay.validation.CustomValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping({"/dashboard/admin", "/dashboard.html/admin"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SessionAttributes({"currentProfile", "clientSender", "message"})
public class AdminDashboardController {

    private final AdminProfileService adminProfileService;
    private final MessageService messageService;
    private final CustomValidationService customValidationService;

    public AdminDashboardController(AdminProfileService adminProfileService, MessageService messageService,
                                    CustomValidationService customValidationService) {
        this.adminProfileService = adminProfileService;
        this.messageService = messageService;
        this.customValidationService = customValidationService;
    }

    /**
     *
     * GET endpoint for the dashboard home page for ADMIN roles.
     *
     * @return
     *  Dashboard Index page (home page).
     *
     */
    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(Model model){
        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());

        model.addAttribute("lastLogin",
                adminProfileService.getLastLogin().format(DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy")));

        model.addAttribute("allUsers", adminProfileService.getAllUserProfiles());

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        model.addAttribute("readMessages", messageService.getReadMessagesCount(currProfile));
        model.addAttribute("unReadMessages", messageService.getUnReadMessagesCount(currProfile));

        return "dashboard/admin/index";
    }

    /**
     *
     * GET endpoint for the My Profile page.
     *
     * @return
     *  My Profile page.
     *
     */
    @GetMapping({"my_profile", "my_profile.html"})
    public String getAdminMyProfilePage(Model model){

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        model.addAttribute("user", currProfile.getUser());
        model.addAttribute("profile", currProfile);
        model.addAttribute("address", currProfile.getAddress());

        return "dashboard/admin/my_profile";
    }

    /**
     *
     * POST endpoint ot update admin profile.
     *
     * @return
     *  My profile page on success and failure.
     *
     */
    @PostMapping({"my_profile", "my_profile.html"})
    public String updateAdminProfile(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                     @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                     @ModelAttribute("address") @Valid Address address, BindingResult addressResult,
                                     Model model, RedirectAttributes ra){

        List<String> formErrors = customValidationService.removeErrors(userResult, profileResult, addressResult);

        if (formErrors.size() > 0) {
            model.addAttribute("formErrors", formErrors);
            return "dashboard/admin/my_profile";
        }

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        adminProfileService.updateAdminProfile(profile, currProfile, user, address);
        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
        ra.addFlashAttribute("updatedProfile", "Admin profile has been updated.");

        return "redirect:/dashboard/admin/my_profile";
    }

    /**
     *
     * GET endpoint for the Users page.
     * GET endpoint to delete users with user id.
     *
     * @return
     *  User page on success and failure.
     *
     */
    @GetMapping({"users", "users.html", "users/{userId}", "users.html/{userId}"})
    public String getAllUsersPage(Model model, @PathVariable(name="userId", required = false) Long userId,
                                    RedirectAttributes ra){

        Profile currProfile = adminProfileService.getAuthenticatedAdminProfile();

        if(userId != null){
            if(currProfile == null || currProfile.getId().equals(userId) || !adminProfileService.deleteProfile(userId)){
                ra.addFlashAttribute("userDeleteError", "User with the id " +  userId + " could not be deleted.");
            }else{
                ra.addFlashAttribute("userDelete", "User with the id " +  userId + " has been deleted.");
                model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
            }
            return "redirect:/dashboard/admin/users";
        }

        model.addAttribute("allUsers", adminProfileService.getAllProfiles());
        return "dashboard/admin/users";
    }

    /**
     *
     *  GET endpoint for the inbox page
     *
     * @return
     *  Inbox page.
     *
     */
    @GetMapping({"inbox", "inbox.html"})
    public String getInboxPage(Model model){
        Profile currProfile = adminProfileService.getAuthenticatedAdminProfile();
        model.addAttribute("currentProfile", currProfile);

        model.addAttribute("allMessages", messageService.getFormattedMessages(currProfile));
        return "dashboard/admin/inbox";
    }

    /**
     *
     * GET endpoint to delete message from the inbox using the message id.
     *
     * @return
     *  Inbox page on success or failure.
     *
     */
    @GetMapping({"inbox/delete/{messageId}", "inbox.html/delete/{messageId}"})
    public String deleteMessage(@PathVariable(name="messageId", required = false) Integer messageId,
                                RedirectAttributes ra, Model model){

        Profile currProfile = adminProfileService.getAuthenticatedAdminProfile();

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
        }else{
            messageService.deleteMessage(currProfile, messageId);
            ra.addFlashAttribute("deleteMessage", "Message with ticket number " + messageId + " has been deleted.");
        }

        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());

        return "redirect:/dashboard/admin/inbox";
    }

    /**
     *
     * GET endpoint for the reply page using the message id.
     *
     * @return
     *  Reply page on success.
     *  Inbox page on failure.
     *
     */
    @GetMapping({"inbox/reply/{messageId}", "inbox.html/reply/{messageId}"})
    public String getReadMessage(@PathVariable(name="messageId", required = false) Integer messageId, Model model,
                                 RedirectAttributes ra){

        Profile currProfile = adminProfileService.getAuthenticatedAdminProfile();

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
            return "dashboard/admin/inbox";
        }

        model.addAttribute("message", messageService.getMessageById(currProfile, messageId));
        model.addAttribute("clientSender", messageService.getSender(currProfile, messageId));

        return "dashboard/admin/reply";
    }

    /**
     *
     * POST endpoint for the reply page. Reply to a user's support message.
     *
     * @return
     *  Reply page on failure.
     *  Inbox page on success.
     *
     */
    @PostMapping({"inbox/reply", "inbox.html/reply"})
    public String sendReplyMessage(Model model, RedirectAttributes ra, @ModelAttribute("message") @Valid Message message,
                                   BindingResult result){

        if(result.hasFieldErrors("replyMessageBody")){
            return "dashboard/admin/reply";
        }

        messageService.sendAdminMessage(message);

        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
        ra.addFlashAttribute("messageSent", "Message with ticket number " + message.getId() + " has been replied to.");
        return "redirect:/dashboard/admin/inbox";
    }

    /**
     *
     * GET endpoint for the support page.
     *
     * @return
     *  Support page.
     *
     */
    @GetMapping({"support", "support.html"})
    public String getAdminSupportPage(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                      @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                      @ModelAttribute("address") @Valid Address address, BindingResult addressResult,
                                      Model model){


        model.addAttribute("allAdmins", adminProfileService.getAllAdminProfiles());
        return "dashboard/admin/support";
    }

    /**
     *
     * POST endpoint for the support page (adding ADMIN profiles).
     *
     * @return
     *  Support page on success and failure.
     *
     */
    @PostMapping({"support", "support.html"})
    public String addNewAdminProfile(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                     @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                     @ModelAttribute("address") @Valid Address address, BindingResult addressResult,
                                     Model model, RedirectAttributes ra){

        List<String> formErrors = customValidationService.removeErrors(userResult, profileResult, addressResult);

        if (formErrors.size() > 0) {

            model.addAttribute("formErrors", formErrors);
            model.addAttribute("allAdmins", adminProfileService.getAllAdminProfiles());
            return "dashboard/admin/support";
        }

        if(!adminProfileService.addAdminProfile(profile, user, address)){

            model.addAttribute("error", "Admin profile already exists.");
            return "dashboard/admin/support";
        }

        ra.addFlashAttribute("createdAdmin",
                "The admin profile for " + profile.getEmail() + " has been created. The admin cannot log in until " +
                        "they set their password. A password reset link has been sent to " + profile.getEmail() + ".");
        return "redirect:/dashboard/admin/support";
    }

    /**
     *
     * POST endpoint for deleting ADMIN profiles.
     *
     * @return
     *  Support page on success and failure.
     *
     */
    @PostMapping({"support/delete/{userId}", "support/delete/{userId}"})
    public String deleteAdminProfile(@PathVariable(name="userId", required = false) Long userId,
                                  RedirectAttributes ra){

        Profile currProfile = adminProfileService.getAuthenticatedAdminProfile();

        if(userId != null){
            if(currProfile == null || currProfile.getId().equals(userId)){
                ra.addFlashAttribute("error", "User with the id " +  userId + " could not be deleted.");
                return "redirect:/dashboard/admin/support";
            }else{

                if(!adminProfileService.deleteProfile(userId)){
                    ra.addFlashAttribute("error", "User with the id " +  userId + " could not be deleted.");
                    return "redirect:/dashboard/admin/support";
                }

                ra.addFlashAttribute("profileDelete", "Admin profile with the id " +  userId + " has been deleted.");
                return "redirect:/dashboard/admin/support";
            }
        }

        ra.addFlashAttribute("error", "User could not be deleted.");
        return "redirect:/dashboard/admin/support";
    }
}





















