/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Controller to mange the routes for the client users.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.services.dashboard.MessageService;
import com.COMP3095.gbc_pay.services.dashboard.user.CreditProfileService;
import com.COMP3095.gbc_pay.services.dashboard.user.UserProfileService;
import com.COMP3095.gbc_pay.validation.CustomValidationService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping({"/dashboard", "/dashboard.html"})
@SessionAttributes({"currentProfile", "currentViewProfile", "allProfiles"})
public class UserDashboardController {

    private final UserProfileService userProfileService;
    private final CreditProfileService creditProfileService;
    private final MessageService messageService;
    private final CustomValidationService customValidationService;

    public UserDashboardController(UserProfileService userProfileService, CreditProfileService creditProfileService,
                                   MessageService messageService, CustomValidationService customValidationService) {
        this.userProfileService = userProfileService;
        this.creditProfileService = creditProfileService;
        this.messageService = messageService;
        this.customValidationService = customValidationService;
    }

    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(Model model){

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());

        model.addAttribute("lastLogin",
                userProfileService.getLastLogin().format(DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy")));
        model.addAttribute("lastUpdate",
                userProfileService.getAuthenticatedProfile().getUser().getLastUpdate()
                        .format(DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy")));

        model.addAttribute("defaultBillingAddress", userProfileService.getBillingAddress());
        model.addAttribute("defaultShippingAddress", userProfileService.getShippingAddress());

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        model.addAttribute("readMessages", messageService.getReadMessagesCount(currProfile));
        model.addAttribute("unReadMessages", messageService.getUnReadMessagesCount(currProfile));

        return "dashboard/user/index";
    }

    @GetMapping({"my_profile", "my_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getMyProfilePage(HttpServletRequest request, Model model, @ModelAttribute("updateEmail") String updateEmail,
                                   @ModelAttribute("user") User user, @ModelAttribute("profile") Profile profile,
                                   @ModelAttribute("address") Address address){

        String email = updateEmail.isEmpty() ? request.getParameter("email") : updateEmail;

        Profile authProfile = userProfileService.getAuthenticatedProfile();

        if(email != null){
            authProfile = userProfileService.getProfileByEmail(email);
        }

        model.addAttribute("user", authProfile.getUser());
        model.addAttribute("profile", authProfile);
        model.addAttribute("address", authProfile.getAddress());

        System.out.println("The view email is " + email);


        model.addAttribute("currentViewProfile", authProfile);
        model.addAttribute("allProfiles", userProfileService.getAllProfiles());

        return "dashboard/user/my_profile";
    }

    @PostMapping({"my_profile/delete", "my_profile.html/delete"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteProfile(HttpServletRequest request, RedirectAttributes ra, Model model){

        String deleteEmail = request.getParameter("delete-email");
        Profile profile = userProfileService.getProfileByEmail(deleteEmail);
        System.out.println("The delete email is " + deleteEmail);

        if(profile.getEmail().equals(userProfileService.getAuthenticatedProfile().getEmail())){
            ra.addFlashAttribute("error", "You cannot delete the current logged in profile.");
            return "redirect:/dashboard/my_profile";
        }

        try {
            userProfileService.deleteProfile(profile);
            model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
            ra.addFlashAttribute("deletedEmail", "Profile with the email '" + deleteEmail + "' has been deleted.");
        }catch (Exception e){
            System.out.println(e.toString());
            ra.addFlashAttribute("deletedEmail", "Profile with the email '" + deleteEmail + "' could not be deleted. Try Again");
        }

        return "redirect:/dashboard/my_profile";
    }

    @PostMapping({"my_profile", "my_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateProfile(RedirectAttributes ra, @ModelAttribute("user") @Valid User user,
                                BindingResult userResult, @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                @ModelAttribute("address") @Valid Address address, BindingResult addressResult, Model model){


        String updateEmail = ((Profile) Objects.requireNonNull(model.getAttribute("currentViewProfile"))).getEmail();


        List<String> formErrors = customValidationService.removeErrors(userResult, profileResult, addressResult);

        if (formErrors.size() > 0) {

            model.addAttribute("formErrors", formErrors);
            ra.addAttribute("updateEmail", updateEmail);

            return "dashboard/user/my_profile";
        }

        Profile tempProfile = (Profile)(model.getAttribute("currentViewProfile"));

        userProfileService.updateProfile(profile, tempProfile, user, address);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());

        ra.addFlashAttribute("updatedProfile", "Profile with the email '" + updateEmail + "' has been updated.");
        ra.addAttribute("updateEmail", updateEmail);

        return "redirect:/dashboard/my_profile";
    }


    @GetMapping({"credit_profile", "credit_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCreditProfilePage(HttpServletRequest request, Model model,
                                       @ModelAttribute("card") Credit credit){

        String cardIdNumber = request.getParameter("cardIdNumber");

        if(cardIdNumber != null){
            System.out.println("Card Number is " + cardIdNumber);

            Profile tempProfile = (Profile)(model.getAttribute("currentProfile"));

            Credit card = creditProfileService.getCreditById(tempProfile, cardIdNumber);

            model.addAttribute("card", card);
            model.addAttribute("defaultCardType", card.getCardType().getCardName());
        }

        return "dashboard/user/credit_profile";
    }

    @PostMapping({"credit_profile", "credit_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addOrUpdateCredit(Model model, RedirectAttributes ra, @ModelAttribute("card") @Valid Credit credit,
                                    BindingResult creditResult){


        if(creditResult.hasErrors()){
            return "dashboard/user/credit_profile";
        }

        System.out.println("Adding or updating credit for " + credit.getCardHolderNumber());

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        creditProfileService.AddOrUpdateCard(currProfile, credit);


        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("updatedCreditProfile", "Credit Profile has been updated.");
        return "redirect:/dashboard/credit_profile";
    }

    @PostMapping({"credit_profile/delete", "credit_profile.html/delete"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteCredit(HttpServletRequest request, RedirectAttributes ra, Model model){

        String deleteCreditId = request.getParameter("delete-id");
        System.out.println("The delete id is " + deleteCreditId);

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        Credit deleteCard = creditProfileService.getCreditById(currProfile, deleteCreditId);

        creditProfileService.deleteCard(currProfile, deleteCard);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("deletedCard", "Credit Card has been deleted.");
        return "redirect:/dashboard/credit_profile";
    }

    @GetMapping({"inbox", "inbox.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getInboxPage(Model model){
        Profile currProfile = userProfileService.getAuthenticatedProfile();

        model.addAttribute("allMessages", messageService.getFormattedMessages(currProfile));
        return "dashboard/user/inbox";
    }

    @GetMapping({"inbox/read/{messageId}", "inbox.html/read/{messageId}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(value = HttpStatus.OK)
    public void getReadMessage(@PathVariable(name="messageId", required = false) Integer messageId, Model model){
        System.out.println("The current read message is " + messageId);

        messageService.readMessage(messageId);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
    }

    @GetMapping({"inbox/delete/{messageId}", "inbox.html/delete/{messageId}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteMessage(@PathVariable(name="messageId", required = false) Integer messageId,
                                RedirectAttributes ra, Model model){

        System.out.println("Deleting Message with the id: " + messageId);

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
        }

        messageService.deleteMessage(currProfile, messageId);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("deleteMessage", "Message with ticket number " + messageId + " has been deleted.");
        return "redirect:/dashboard/inbox";
    }

    @GetMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getSupportPage(@ModelAttribute("message") Message message){
        return "dashboard/user/support";
    }

    @PostMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String sendMessage(Model model, RedirectAttributes ra, @ModelAttribute("message") @Valid Message message, BindingResult result){
        System.out.println("Sending a message with subject: " + message.getSubject());

         if(result.hasFieldErrors("subject") || result.hasFieldErrors("messageBody")){
             return "dashboard/user/support";
         }

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        messageService.sendMessage(currProfile, message);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("messageSent", "Your message has been sent.");
        return "redirect:/dashboard/inbox";
    }



























}










