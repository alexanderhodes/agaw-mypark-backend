package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PasswordResetService {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    public User requestPasswordReset (String email) {
        // 1. Prüfen, ob Benutzer existiert
        User user = userService.findByUsername(email);
        // 2. Token generieren und in DB speichern
        System.out.println(user);
        // 3. E-Mail senden
        try {
            mailService.send("test@email.com", email, "Password reset", "Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

}
