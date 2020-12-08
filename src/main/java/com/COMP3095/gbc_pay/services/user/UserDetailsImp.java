package com.COMP3095.gbc_pay.services.user;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {

    private final Profile profile;

    public UserDetailsImp(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        profile.getRoles()
        .forEach(r -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getRoleName()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return profile.getUser();
    }

    public Profile getProfile() { return profile; }

    public boolean isAdminAccount(){
        return profile.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equals("ADMIN"));
    }
}
