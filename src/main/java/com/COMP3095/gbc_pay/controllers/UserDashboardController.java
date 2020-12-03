package com.COMP3095.gbc_pay.controllers;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Credit;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.services.dashboard.user.CreditProfileService;
import com.COMP3095.gbc_pay.services.dashboard.user.UserMessageService;
import com.COMP3095.gbc_pay.services.dashboard.user.UserProfileService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
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
    private final UserMessageService userMessageService;

    public UserDashboardController(UserProfileService userProfileService, CreditProfileService creditProfileService,
                                   UserMessageService userMessageService) {
        this.userProfileService = userProfileService;
        this.creditProfileService = creditProfileService;
        this.userMessageService = userMessageService;
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
    public String updateProfile(HttpServletRequest request, RedirectAttributes ra, @ModelAttribute("user") @Valid User user,
                                BindingResult userResult, @ModelAttribute("profile") @Valid Profile profile, BindingResult profileResult,
                                @ModelAttribute("address") @Valid Address address, BindingResult addressResult, Model model){


        String updateEmail = ((Profile) Objects.requireNonNull(model.getAttribute("currentViewProfile"))).getEmail();

        List<ObjectError> filteredProfileErrors = new ArrayList<>();
        if(profileResult.getErrorCount() > 0){
            for (ObjectError o: profileResult.getAllErrors()){
                if( !Objects.equals(o.getDefaultMessage(), "Confirm Password cannot be empty")
                        && !Objects.equals(o.getDefaultMessage(), "Must agree to terms of service")
                        && !Objects.equals(o.getDefaultMessage(), "Password fields must match")){
                    filteredProfileErrors.add(o);
                }
            }
        }

        if (userResult.hasErrors() || filteredProfileErrors.size() > 0 || addressResult.hasErrors()) {

            List<String> profileErrorMessages = filteredProfileErrors.stream()
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
    public String getInboxPage(){
        return "dashboard/user/inbox";
    }





















    @GetMapping({"support", "support.html"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getSupportPage(){
        return "dashboard/support";
    }
}
