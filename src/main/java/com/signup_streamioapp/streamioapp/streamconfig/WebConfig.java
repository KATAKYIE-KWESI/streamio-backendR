package com.signup_streamioapp.streamioapp.streamconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Change to your frontend URL in production
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
