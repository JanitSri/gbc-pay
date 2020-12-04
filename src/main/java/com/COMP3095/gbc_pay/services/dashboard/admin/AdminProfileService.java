package com.COMP3095.gbc_pay.services.dashboard.admin;

import com.COMP3095.gbc_pay.models.Profile;
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

    public AdminProfileService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public Profile getAuthenticatedAdminProfile(){
        Profile p = ((UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        return userService.findByEmail(p.getEmail());
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

        userService.deleteProfile(profile);
        return true;
    }
}












