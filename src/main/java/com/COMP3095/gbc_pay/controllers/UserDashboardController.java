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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;



@Controller
@RequestMapping({"/dashboard", "/dashboard.html"})
@SessionAttributes({"currentProfile", "currentViewProfile", "allProfiles", "portNumber"})
public class UserDashboardController {

    private final UserProfileService userProfileService;
    private final CreditProfileService creditProfileService;
    private final MessageService messageService;
    private final CustomValidationService customValidationService;

    @Value("${server.port}")
    private int portNumber;

    public UserDashboardController(UserProfileService userProfileService, CreditProfileService creditProfileService,
                                   MessageService messageService, CustomValidationService customValidationService) {
        this.userProfileService = userProfileService;
        this.creditProfileService = creditProfileService;
        this.messageService = messageService;
        this.customValidationService = customValidationService;
    }

    /**
     *
     * GET endpoint for the dashboard index page for the USER role.
     *
     * @return
     *  Dashboard Index page (home page).
     *
     */
    @GetMapping({"", "/index.html", "index"})
    public String getIndexPage(Model model){

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());

        model.addAttribute("portNumber", portNumber);

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

    /**
     *
     * GET endpoint for the My Profile page.
     *
     * @return
     *  My Profile page.
     *
     */
    @GetMapping({"my_profile", "my_profile.html"})
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

        model.addAttribute("currentViewProfile", authProfile);
        model.addAttribute("allProfiles", userProfileService.getAllProfiles());

        return "dashboard/user/my_profile";
    }

    /**
     *
     * POST endpoint for My Profile page to delete users.
     *
     * @return
     *  My Profile page on success and failure.
     *
     */
    @PostMapping({"my_profile/delete", "my_profile.html/delete"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteProfile(HttpServletRequest request, RedirectAttributes ra, Model model){

        String deleteEmail = request.getParameter("delete-email");
        Profile profile = userProfileService.getProfileByEmail(deleteEmail);

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

    /**
     *
     * POST endpoint for My Profile to update a profile.
     *
     * @return
     *  My Profile page on success and failure.
     *
     */
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

    /**
     *
     * GET endpoint for the Credit Profile page.
     *
     * @return
     *  Credit Profile page.
     *
     */
    @GetMapping({"credit_profile", "credit_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCreditProfilePage(HttpServletRequest request, Model model,
                                       @ModelAttribute("card") Credit credit){

        String cardIdNumber = request.getParameter("cardIdNumber");

        if(cardIdNumber != null){

            Profile tempProfile = (Profile)(model.getAttribute("currentProfile"));

            Credit card = creditProfileService.getCreditById(tempProfile, cardIdNumber);

            model.addAttribute("card", card);
            model.addAttribute("defaultCardType", card.getCardType().getCardName());
        }

        return "dashboard/user/credit_profile";
    }

    /**
     *
     * POST end point for the Credit Profile page, to add or update a credit profile.
     *
     * @return
     *  Credit profile page on success or failure.
     *
     */
    @PostMapping({"credit_profile", "credit_profile.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String addOrUpdateCredit(Model model, RedirectAttributes ra, @ModelAttribute("card") @Valid Credit credit,
                                    BindingResult creditResult){


        if(creditResult.hasErrors()){
            return "dashboard/user/credit_profile";
        }

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        creditProfileService.AddOrUpdateCard(currProfile, credit);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("updatedCreditProfile", "Credit Profile has been updated.");
        return "redirect:/dashboard/credit_profile";
    }

    /**
     *
     * POST endpoint to delete credit profile.
     *
     * @return
     *  Redirect to Credit Profile page on success or failure.
     *
     */
    @PostMapping({"credit_profile/delete", "credit_profile.html/delete"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteCredit(HttpServletRequest request, RedirectAttributes ra, Model model){

        String deleteCreditId = request.getParameter("delete-id");

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));
        Credit deleteCard = creditProfileService.getCreditById(currProfile, deleteCreditId);

        if(deleteCard == null){
            ra.addFlashAttribute("error", "Credit Card could not be deleted.");
            return "redirect:/dashboard/credit_profile";
        }


        creditProfileService.deleteCard(currProfile, deleteCard);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("deletedCard", "Credit Card has been deleted.");
        return "redirect:/dashboard/credit_profile";
    }

    /**
     *
     * GET endpoint for the Inbox page, support messages that have been sent and the replies.
     *
     * @return
     *  Inbox Page.
     *
     */
    @GetMapping({"inbox", "inbox.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getInboxPage(Model model){
        Profile currProfile = userProfileService.getAuthenticatedProfile();

        model.addAttribute("allMessages", messageService.getFormattedMessages(currProfile));
        return "dashboard/user/inbox";
    }

    /**
     *
     * GET endpoint to update the read status of the message.
     *
     */
    @GetMapping({"inbox/read/{messageId}", "inbox.html/read/{messageId}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(value = HttpStatus.OK)
    public void getReadMessage(@PathVariable(name="messageId", required = false) Integer messageId, Model model){

        messageService.readMessage(messageId);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
    }

    /**
     *
     * GET endpoint to delete a message by message id.
     *
     * @return
     *  Inbox Page on success or faliure.
     *
     */
    @GetMapping({"inbox/delete/{messageId}", "inbox.html/delete/{messageId}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String deleteMessage(@PathVariable(name="messageId", required = false) Integer messageId,
                                RedirectAttributes ra, Model model){

        Profile currProfile = (Profile)(model.getAttribute("currentProfile"));

        if(messageId == null || messageService.getMessageById(currProfile, messageId) == null){
            ra.addFlashAttribute("error", "Message could not be deleted.");
            return "redirect:/dashboard/inbox";
        }

        messageService.deleteMessage(currProfile, messageId);

        model.addAttribute("currentProfile", userProfileService.getAuthenticatedProfile());
        ra.addFlashAttribute("deleteMessage", "Message with ticket number " + messageId + " has been deleted.");
        return "redirect:/dashboard/inbox";
    }

    /**
     *
     * GET endpoint for the Support Page to send support messages.
     *
     * @return
     *  Support Page
     */
    @GetMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getSupportPage(@ModelAttribute("message") Message message){
        return "dashboard/user/support";
    }

    /**
     *
     * POST endpoint to send message with validation.
     *
     * @return
     *  Support page on failure.
     *  Inbox Page on success.
     *
     */
    @PostMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String sendMessage(Model model, RedirectAttributes ra, @ModelAttribute("message") @Valid Message message, BindingResult result){

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










