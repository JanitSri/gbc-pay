package com.COMP3095.gbc_pay.services.dashboard;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.repositories.ProfileRepository;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class DashboardUserService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public DashboardUserService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Profile getAuthenticatedProfile(){
        return ((UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
    }

    public LocalDate getLastLogin(){

        Profile currentProfile = getAuthenticatedProfile();
        LocalDate previousLogin = currentProfile.getUser().getLastLogin();

        currentProfile.getUser().setLastLogin(LocalDate.now());
        userRepository.save(currentProfile.getUser());

        if(previousLogin == null){
            previousLogin = LocalDate.now();
        }

        return previousLogin;
    }

    public Address getBillingAddress(){
        Set<Profile> profiles = getAuthenticatedProfile().getUser().getProfiles();

        Profile preferredBillingAddressProfile = profiles.stream()
                .filter(profile -> profile.getAddress().isDefaultBilling()).findFirst().orElse(null);

        return preferredBillingAddressProfile != null ? preferredBillingAddressProfile.getAddress() : null;
    }

    public Address getShippingAddress(){
        Set<Profile> profiles = getAuthenticatedProfile().getUser().getProfiles();

        Profile preferredShippingAddressProfile = profiles.stream()
                .filter(profile -> profile.getAddress().isDefaultBilling()).findFirst().orElse(null);

        return preferredShippingAddressProfile != null ? preferredShippingAddressProfile.getAddress() : null;
    }



}




























