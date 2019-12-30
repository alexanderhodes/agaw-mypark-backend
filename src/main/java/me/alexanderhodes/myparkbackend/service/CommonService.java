package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.helper.FormDataHandler;
import me.alexanderhodes.myparkbackend.helper.UrlHelper;
import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.mail.MailHelper;
import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.mail.model.MMail;
import me.alexanderhodes.myparkbackend.model.Role;
import me.alexanderhodes.myparkbackend.model.Token;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.UserRole;
import me.alexanderhodes.myparkbackend.translations.EmailTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

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
    private TokenService tokenService;
    @Autowired
    private FormDataHandler formDataHandler;
    @Autowired
    private UrlHelper urlHelper;
    @Autowired
    private MailHelper mailHelper;
    @Value("${mypark.production}")
    private boolean production;
    @Value("${mypark.sendmail}")
    private boolean sendMail;

    public User requestPasswordReset (String email) {
        // 1. Prüfen, ob Benutzer existiert
        User user = userService.findByUsername(email);

        if (user != null) {
            // 2. Token generieren und in DB speichern
            String base64token = createToken(email, user, Token.PASSWORD_RESET);
            // 3. E-Mail senden
            sendMailWithToken(user, base64token);

            return user.toJson();
        }

        return null;
    }

    public User validateToken (String base64Token) {
        String id = uuidGenerator.getIdFromBase64Token(base64Token);
        Optional<Token> optional = tokenService.findById(id);

        return optional.isPresent() ? optional.get().getUser().toJson() : null;
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
            // 2. Token generieren und in DB speichern
            String base64token = createToken(user.getUsername(), user, Token.REGISTRATION);
            // 3. E-Mail senden
            sendMailWithToken(user, base64token);

            return user;
        }

        return null;
    }

    public void activateUser (User user) {
        user.setEnabled(true);
        this.userService.save(user);
    }

    public void deleteToken (String base64Token) {
        String id = uuidGenerator.getIdFromBase64Token(base64Token);

        this.tokenService.deleteById(id);
    }

    private String createToken(String email, User user, String tokenType) {
        String base64token = "";
        try {
            base64token = uuidGenerator.newBase64Token(email);
            LocalDateTime localDateTime = LocalDateTime.now();
            localDateTime = localDateTime.plusDays(1);

            String uuid = uuidGenerator.getIdFromBase64Token(base64token);
            Token token = new Token(uuid, user, localDateTime, tokenType);
            tokenService.save(token);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64token;
    }

    private void sendMailWithToken(User user, String base64token) {
        try {
            String placeholderLink = this.urlHelper.getPasswordResetUrl(base64token);
            List<AbstractMap.SimpleEntry<String, String>> placeholders = new ArrayList<>();
            placeholders.add(new AbstractMap.SimpleEntry("PLACEHOLDER_USERNAME", user.getUsername()));
            placeholders.add(new AbstractMap.SimpleEntry("PLACEHOLDER_LINKTOKEN", placeholderLink));

            MMail mmail = mailHelper.createMail("alexander.hodes@live.com", EmailTranslations.RESET_PASSWORD,
                    placeholders);
            if (sendMail) {
                mailService.send(mmail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
