package com.COMP3095.gbc_pay.services.dashboard.admin;

import com.COMP3095.gbc_pay.models.Address;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.security.SessionUtils;
import com.COMP3095.gbc_pay.services.user.RoleService;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminProfileService {

    private final UserService userService;
    private final RoleService roleService;
    private final SessionUtils sessionUtils;

    public AdminProfileService(UserService userService, RoleService roleService, SessionUtils sessionUtils) {
        this.userService = userService;
        this.roleService = roleService;
        this.sessionUtils = sessionUtils;
    }

    public Profile getAuthenticatedAdminProfile(){
        Profile p = ((UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        return userService.findByProfileById(p.getId());
    }

    public LocalDate getLastLogin(){

        Profile currentProfile = getAuthenticatedAdminProfile();
        LocalDate previousLogin = currentProfile.getUser().getLastLogin();

        currentProfile.getUser().setLastLogin(LocalDate.now());
        userService.saveUser(currentProfile.getUser());

        if(previousLogin == null){
            previousLogin = LocalDate.now();
        }

        return previousLogin;
    }

    public List<Profile> getAllUserProfiles(){
        return new ArrayList<>(roleService.getRole("USER").getProfiles());
    }

    public List<Profile> getAllProfiles(){
        return userService.getAllProfiles();
    }

    public boolean deleteProfile(Long profileId){

        Profile profile = userService.findByProfileById(profileId);

        if(profile == null){
            return false;
        }

        sessionUtils.expireUserSession(profile.getId());

        userService.deleteProfile(profile);
        return true;
    }

    public void updateAdminProfile(Profile updateProfile, Profile existingProfile, User updateUser, Address updateAddress){
        User existingUser = userService.findByUser(existingProfile.getUser());

        existingUser.setFirstName(updateUser.getFirstName());
        existingUser.setLastName(updateUser.getLastName());
        existingUser.setDateOfBirth(updateUser.getDateOfBirth());

        for(Profile p : existingUser.getProfiles()){
            if(p.getEmail().equals(existingProfile.getEmail())){
                p.getAddress().setCity(updateAddress.getCity());
                p.getAddress().setCountry(updateAddress.getCountry());
                p.getAddress().setStreet(updateAddress.getStreet());

                p.setEmail(updateProfile.getEmail());
            }
        }

        userService.saveUser(existingUser);
    }
}












