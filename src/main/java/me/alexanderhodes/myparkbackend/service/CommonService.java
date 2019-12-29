package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.helper.FileImport;
import me.alexanderhodes.myparkbackend.helper.FormDataHandler;
import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.model.Role;
import me.alexanderhodes.myparkbackend.model.Token;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.UserRole;
import me.alexanderhodes.myparkbackend.translations.EmailTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
    @Autowired
    private FileImport fileImport;
    @Autowired
    private EmailTranslations emailTranslations;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private FormDataHandler formDataHandler;

    public User requestPasswordReset (String email) {
        // 1. Prüfen, ob Benutzer existiert
        User user = userService.findByUsername(email);

        if (user != null) {
            // 2. Token generieren und in DB speichern
            String base64token = "";
            try {
                base64token = uuidGenerator.newBase64Token(email);
                System.out.println("token: " + base64token);

                LocalDateTime localDateTime = LocalDateTime.now();
                localDateTime = localDateTime.plusDays(1);

                String uuid = uuidGenerator.getIdFromBase64Token(base64token);

                Token token = new Token(uuid, user, localDateTime);
                tokenService.save(token);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 3. E-Mail senden
            try {
                String htmlBody = fileImport.getText("email-template.html");
                String content = emailTranslations.getContent(EmailTranslations.RESET_PASSWORD);
                String headline = emailTranslations.getHeadline(EmailTranslations.RESET_PASSWORD);
                String subject = emailTranslations.getSubject(EmailTranslations.RESET_PASSWORD);
                htmlBody = htmlBody.replace("PLACEHOLDER_USERNAME", user.getUsername())
                                    .replace("PLACEHOLDER_HEADLINE", headline)
                                    .replace("PLACEHOLDER_CONTENT", content);
                mailService.send("alexander.hodes@live.com", subject, htmlBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user.toJson();
        }

        return null;
    }

    public boolean validateToken (String base64Token) {
        String id = uuidGenerator.getIdFromBase64Token(base64Token);
        Optional<Token> optional = tokenService.findById(id);

        return optional.isPresent();
    }

    public boolean storePassword (String base64Token, String formData) {
        String username = uuidGenerator.getUsernameFromBase64Token(base64Token);
        User user = userService.findByUsername(username);

        if (user != null) {
            String password = formDataHandler.extract(formData, 3);
            String passwordEncoded = new BCryptPasswordEncoder().encode(password);
            user.setPassword(passwordEncoded);

            userService.save(user);

            return true;
        }
        return false;
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
                mailService.send("alexander.hodes@live.com", "Registration", "Hi, you're registered!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return user;
        }

        return null;
    }

}
