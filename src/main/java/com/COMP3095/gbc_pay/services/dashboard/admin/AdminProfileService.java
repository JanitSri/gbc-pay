package com.COMP3095.gbc_pay.services.dashboard.admin;

import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.security.SessionUtils;
import com.COMP3095.gbc_pay.services.account.PasswordResetService;
import com.COMP3095.gbc_pay.services.user.RoleService;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminProfileService {

    private final UserService userService;
    private final RoleService roleService;
    private final SessionUtils sessionUtils;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    public AdminProfileService(UserService userService, RoleService roleService, SessionUtils sessionUtils,
                               PasswordEncoder passwordEncoder, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.roleService = roleService;
        this.sessionUtils = sessionUtils;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
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

    public List<Profile> getAllAdminProfiles(){
        return new ArrayList<>(roleService.getRole("ADMIN").getProfiles());
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

    public boolean addAdminProfile(Profile newProfile, User newUser, Address newAddress){

        Profile profile = userService.findByEmail(newProfile.getEmail());

        if(profile != null){
            return false;
        }

        newAddress.setDefaultBilling(false);
        newAddress.setDefaultShipping(false);

        newProfile.setEmailVerified(true);

        final String tempPassword = UUID.randomUUID().toString().replace("-", "");
        newProfile.setPassword(passwordEncoder.encode(tempPassword));

        newProfile.setAddress(newAddress);
        newAddress.setProfile(newProfile);

        Role adminRole = roleService.getRole("ADMIN");

        newProfile.getRoles().add(adminRole);
        adminRole.getProfiles().add(newProfile);

        newProfile.setUser(newUser);
        newUser.getProfiles().add(newProfile);

        userService.saveUser(newUser);

        try {
            passwordResetService.sendResetPasswordLink(newProfile);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}












