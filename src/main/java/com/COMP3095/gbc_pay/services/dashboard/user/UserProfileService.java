package com.COMP3095.gbc_pay.services.dashboard.user;

import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class UserProfileService {

    private final UserService userService;

    public UserProfileService(UserService userService) {
        this.userService = userService;
    }

    public Profile getAuthenticatedProfile(){
        Profile p = ((UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        return userService.findByEmail(p.getEmail());
    }

    public LocalDate getLastLogin(){

        Profile currentProfile = getAuthenticatedProfile();
        LocalDate previousLogin = currentProfile.getUser().getLastLogin();

        currentProfile.getUser().setLastLogin(LocalDate.now());
        userService.saveUser(currentProfile.getUser());

        if(previousLogin == null){
            previousLogin = LocalDate.now();
        }

        return previousLogin;
    }

    public Profile getDefaultBillingProfile(){
        Set<Profile> profiles = getAuthenticatedProfile().getUser().getProfiles();

        return profiles.stream()
                .filter(profile -> profile.getAddress().isDefaultBilling()).findFirst().orElse(null);
    }

    public Address getBillingAddress(){
        Profile preferredBillingAddressProfile = getDefaultBillingProfile();

        return preferredBillingAddressProfile != null ? preferredBillingAddressProfile.getAddress() : null;
    }

    public Profile getDefaultShippingProfile(){
        Set<Profile> profiles = getAuthenticatedProfile().getUser().getProfiles();
        return profiles.stream()
                .filter(profile -> profile.getAddress().isDefaultShipping()).findFirst().orElse(null);
    }

    public Address getShippingAddress(){
        Profile preferredShippingAddressProfile = getDefaultShippingProfile();

        return preferredShippingAddressProfile != null ? preferredShippingAddressProfile.getAddress() : null;
    }

    public Set<Profile> getAllProfiles(){
        return getAuthenticatedProfile().getUser().getProfiles();
    }

    public Profile getProfileByEmail(String email){

        Profile profile = userService.findByEmail(email);
        if(profile == null){
            profile = getAuthenticatedProfile();
        }
        return profile;
    }

    public void deleteProfile(Profile profile){
        userService.deleteProfile(profile);
    }

    public void updateProfile(Profile updateProfile, Profile existingProfile, User updateUser, Address updateAddress){
        User existingUser = userService.findByUser(existingProfile.getUser());

        Profile preferredBillingAddressProfile = getDefaultBillingProfile();
        Profile preferredShippingAddressProfile = getDefaultShippingProfile();


        if(preferredBillingAddressProfile != null && !preferredBillingAddressProfile.getEmail().equals(updateProfile.getEmail())
                && updateAddress.isDefaultBilling()
        ){
            System.out.println("Changing Billing Address");
            preferredBillingAddressProfile.getAddress().setDefaultBilling(false);
            userService.saveProfile(preferredBillingAddressProfile);
        }


        if(preferredShippingAddressProfile != null && !preferredShippingAddressProfile.getEmail().equals(updateProfile.getEmail())
                && updateAddress.isDefaultShipping()
        ){
            System.out.println("Changing Shipping Address");
            preferredShippingAddressProfile.getAddress().setDefaultShipping(false);
            userService.saveProfile(preferredShippingAddressProfile);
        }

        existingUser.setFirstName(updateUser.getFirstName());
        existingUser.setLastName(updateUser.getLastName());
        existingUser.setDateOfBirth(updateUser.getDateOfBirth());

        for(Profile p : existingUser.getProfiles()){
            if(p.getEmail().equals(existingProfile.getEmail())){
                p.getAddress().setDefaultBilling(updateAddress.isDefaultBilling());
                p.getAddress().setDefaultShipping(updateAddress.isDefaultShipping());
                p.getAddress().setCity(updateAddress.getCity());
                p.getAddress().setCountry(updateAddress.getCountry());
                p.getAddress().setStreet(updateAddress.getStreet());

                p.setEmail(updateProfile.getEmail());
            }
        }

        userService.saveUser(existingUser);
    }

}




























