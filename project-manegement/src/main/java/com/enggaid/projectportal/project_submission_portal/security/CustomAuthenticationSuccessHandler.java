package com.enggaid.projectportal.project_submission_portal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities =authentication.getAuthorities();
        for (GrantedAuthority authority: authorities){
            String role = authority.getAuthority();

            if(role.equals("ROLE_ADMIN")){
                response.sendRedirect("/admin/dashboard");
                return;
            } else if (role.equals("ROLE_HOD")) {
                response.sendRedirect("/hod/dashboard");
                return;

            } else if (role.equals("ROLE_MENTOR")) {
                response.sendRedirect("/mentor/dashboard");
                return;
            } else if (role.equals("ROLE_PROJECTHEAD")) {
                response.sendRedirect("/projecthead/dashboard");
                return;
            }
        }

        //Default Redirect
        response.sendRedirect("/home");
    }
}
