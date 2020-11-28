package com.COMP3095.gbc_pay.security;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.services.user.UserDetailsImp;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {


        Profile userProfile = ((UserDetailsImp) authentication.getPrincipal()).getProfile();
        User user = userProfile.getUser();

        user.setPreviousLoginDate(user.getLastLogin());

        if(user.getPreviousLoginDate() == null){
            user.setPreviousLoginDate(LocalDate.now());
        }

        httpServletResponse.sendRedirect("/dashboard");
    }
}
