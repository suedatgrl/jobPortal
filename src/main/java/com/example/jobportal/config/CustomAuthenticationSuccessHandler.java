package com.example.jobportal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {

        UserDetails userDetails=  (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("The username " + username + "is logged in");

        boolean hasJobSeekerRole =authentication.getAuthorities().stream() //r->r is a lambda expression.
                .anyMatch(r-> r.getAuthority().equals("Job Seeker"));

        boolean hasRecruiterRole =authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("Recruiter")); //getauthority method coming from the granted authority interface. granted authority interface is coming from the spring security core.

        if(hasRecruiterRole || hasJobSeekerRole){
            response.sendRedirect(("/dashboard/")); //redirect means to move to the new page.
        }else {
            System.out.println("No valid role found! Redirecting to login...");
            response.sendRedirect(request.getContextPath() + "/login?error");
        }

    }
}
