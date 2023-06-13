package com.rodrigopeleias.bookstoremanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncondingConfig {

    @Bean
    public PasswordEncoder passwordEnconder() {
        return new BCryptPasswordEncoder();
    }

}
