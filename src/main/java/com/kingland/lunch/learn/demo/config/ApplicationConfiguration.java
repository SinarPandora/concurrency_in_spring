package com.kingland.lunch.learn.demo.config;

import java.util.Locale;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public Faker faker() {
        return new Faker(Locale.ENGLISH);
    }
}
