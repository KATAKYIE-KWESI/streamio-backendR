package com.signup_streamioapp.streamioapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://172.20.10.3:19006", // Your React Native or Expo frontend IP + port
                                "http://localhost:19006" // Optional: for local web testing
                )
                        .allowedMethods("*") // Allow all HTTP methods (GET, POST, etc)
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // If you want to allow cookies/auth headers
            }
        };
    }
}
