package me.alexanderhodes.myparkbackend.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration()
public class AuthenticationService {

    public String getCurrentUsername () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return currentPrincipalName;
    }

}
