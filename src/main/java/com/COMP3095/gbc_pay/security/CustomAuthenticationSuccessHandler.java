package com.COMP3095.gbc_pay.security;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {


        Profile userProfile = ((UserDetailsImp) authentication.getPrincipal()).getProfile();

        boolean isAdmin = userProfile.getRoles()
                            .stream()
                            .anyMatch(role -> role.getRoleName().equals("ADMIN"));

        if(!isAdmin){
            httpServletResponse.sendRedirect("/dashboard");
        }else{
            httpServletResponse.sendRedirect("/dashboard/admin");
        }
    }
}
