/* *********************************************************************************
* Project: GBC PAY - The Raptors
* Assignment: Assignment 3
* Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
* Student Number: 101229102, 101186743, 101028504
* Date: December 05, 2020
* Description: Custom Authentication Entry point to manage failure to login by user.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        redirectStrategy.sendRedirect(request, response, "/denied_login");
    }
}
