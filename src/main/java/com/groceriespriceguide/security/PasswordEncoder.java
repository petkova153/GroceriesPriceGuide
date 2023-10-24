package com.groceriespriceguide.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordEncoder {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String encode(String passWord) {
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(passWord);

    }

    public boolean matches(String plainPassword, String hashedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(plainPassword, hashedPassword);
    }
}
