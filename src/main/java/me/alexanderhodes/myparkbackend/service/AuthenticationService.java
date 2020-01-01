package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration()
public class AuthenticationService {

    @Autowired
    private UserService userService;

    public String getCurrentUsername () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return currentPrincipalName;
    }

    public User getCurrentUser () {
        String username = this.getCurrentUsername();

        return (username != null && !username.isEmpty()) ? userService.findByUsername(username) : null;
    }

}
