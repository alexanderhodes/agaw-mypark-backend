package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.model.Role;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
public class CommonService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UuidGenerator uuidGenerator;

    public User requestPasswordReset (String email) {
        // 1. Prüfen, ob Benutzer existiert
        User user = userService.findByUsername(email);

        if (user != null) {
            // 2. Token generieren und in DB speichern
            String token = "";
            try {
                token = uuidGenerator.newBase64Token(email);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 3. E-Mail senden
            try {
                mailService.send("test@email.com", email, "Password reset", "Hello World!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user.toJson();
        }

        return null;
    }

    public User createUser (User body) {
        User userExists = userService.findByUsername(body.getUsername());

        if (userExists == null) {
            // hashing password
            String password = new BCryptPasswordEncoder().encode(body.getPassword());
            body.setPassword(password);
            // create body
            userService.save(body);
            User user = userService.findByUsername(body.getUsername());
            // create roles
            Optional<Role> role = roleService.findById("USER");
            UserRole userRole = new UserRole(user, role.get());
            userRoleService.save(userRole);
            // send registration mail
            try {
                mailService.send("test@email.com", "alexander.hodes@live.com", "Registration", "Hi, you're registered!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return user;
        }

        return null;
    }

}
