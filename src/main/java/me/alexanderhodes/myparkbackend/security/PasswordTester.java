package me.alexanderhodes.myparkbackend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTester {


    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("Telekom123"));
    }

}
