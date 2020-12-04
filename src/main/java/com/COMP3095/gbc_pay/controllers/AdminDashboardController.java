package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Message;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.services.dashboard.MessageService;
import com.COMP3095.gbc_pay.services.dashboard.admin.AdminProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping({"/dashboard/admin", "/dashboard.html/admin"})
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SessionAttributes({"currentProfile", "clientSender", "message"})
public class AdminDashboardController {

    private final AdminProfileService adminProfileService;
    private final MessageService messageService;

    public AdminDashboardController(AdminProfileService adminProfileService, MessageService messageService) {
        this.adminProfileService = adminProfileService;
        this.messageService = messageService;
    }


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






    @GetMapping({"my_profile", "my_profile.html"})
    public String getAdminMyProfilePage(Model model){

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        model.addAttribute("user", currProfile.getUser());
        model.addAttribute("profile", currProfile.getUser());
        model.addAttribute("address", currProfile.getAddress());


        return "dashboard/admin/my_profile";
    }

    @PostMapping({"my_profile", "my_profile.html"})
    public String updateAdminProfile(@ModelAttribute("user") @Valid User user, BindingResult userResult,
                                     @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                     @ModelAttribute("address") @Valid Address address, BindingResult addressResult,
                                     Model model){

        System.out.println("Updating the ADMIN profile of" + profile.getEmail());
        return "dashboard/admin/my_profile";
    }























    @GetMapping({"users", "users.html", "users/{userId}", "users.html/{userId}"})
    public String getAdminUsersPage(Model model, @PathVariable(name="userId", required = false) Long userId,
                                    RedirectAttributes ra){

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        if(userId != null){
            if(currProfile == null || currProfile.getId().equals(userId)){
                ra.addFlashAttribute("userDeleteError", "User with the id " +  userId + " could not be deleted.");
                return "redirect:/dashboard/admin/users";
            }else{
                System.out.println("Deleting user with the id " + userId);

                if(!adminProfileService.deleteProfile(userId)){
                    ra.addFlashAttribute("userDeleteError", "User with the id " +  userId + " could not be deleted.");
                    return "redirect:/dashboard/admin/users";
                }

                ra.addFlashAttribute("userDelete", "User with the id " +  userId + " has been deleted.");
                model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
                return "redirect:/dashboard/admin/users";
            }
        }

        model.addAttribute("allUsers", adminProfileService.getAllProfiles());
        return "dashboard/admin/users";
    }

    @GetMapping({"inbox", "inbox.html"})
    public String getInboxPage(Model model){
        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        model.addAttribute("allMessages", messageService.getFormattedMessages(currProfile));
        return "dashboard/admin/inbox";
    }

    @GetMapping({"inbox/delete/{messageId}", "inbox.html/delete/{messageId}"})
    public String deleteMessage(@PathVariable(name="messageId", required = false) Integer messageId,
                                RedirectAttributes ra, Model model){

        System.out.println("ADMIN --- Deleting Message with the id: " + messageId);

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
        }

        messageService.deleteMessage(currProfile, messageId);

        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
        ra.addFlashAttribute("deleteMessage", "Message with ticket number " + messageId + " has been deleted.");
        return "redirect:/dashboard/admin/inbox";
    }

    @GetMapping({"inbox/reply/{messageId}", "inbox.html/reply/{messageId}"})
    public String getReadMessage(@PathVariable(name="messageId", required = false) Integer messageId, Model model,
                                 RedirectAttributes ra){
        System.out.println("ADMIN --- Replying to Message " + messageId);

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
            return "dashboard/admin/inbox";
        }

        model.addAttribute("message", messageService.getMessageById(currProfile, messageId));
        model.addAttribute("clientSender", messageService.getSender(currProfile, messageId));

        return "dashboard/admin/reply";
    }

    @PostMapping({"inbox/reply", "inbox.html/reply"})
    public String sendReplyMessage(Model model, RedirectAttributes ra, @ModelAttribute("message") @Valid Message message,
                                   BindingResult result){
        System.out.println("ADMIN --- Sending Message with ticket number " + message.getId());

        if(result.hasFieldErrors("replyMessageBody")){
            return "dashboard/admin/reply";
        }

        messageService.sendAdminMessage(message);

        model.addAttribute("currentProfile", adminProfileService.getAuthenticatedAdminProfile());
        ra.addFlashAttribute("messageSent", "Message with ticket number " + message.getId() + " has been replied to.");
        return "redirect:/dashboard/admin/inbox";
    }


    @GetMapping({"support", "support.html"})
    public String getAdminSupportPage(){
        return "dashboard/support";
    }
}
