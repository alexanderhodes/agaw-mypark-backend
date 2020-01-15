package me.alexanderhodes.myparkbackend.helper;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration()
public class PasswordHelper {

    public String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public boolean matches(String password, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(password, encodedPassword);
    }

}
