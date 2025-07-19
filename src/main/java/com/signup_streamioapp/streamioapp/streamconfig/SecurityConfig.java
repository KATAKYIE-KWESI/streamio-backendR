package com.signup_streamioapp.streamioapp.streamconfig;

import com.signup_streamioapp.streamioapp.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                                .authorizeHttpRequests(auth -> auth
                                                // Allow CORS preflight
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // Public endpoints
                                                .requestMatchers(
                                                                "/api/v1/auth/register",
                                                                "/api/v1/auth/authenticate",
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/forgot-password",
                                                                "/api/v1/auth/reset-password",
                                                                "/api/v1/confirm-account",
                                                                "/uploads/**" // ✅ Allow access to uploaded images
                                                ).permitAll()

                                                // Profile endpoints can be publicly accessible for now or token
                                                // protected
                                                .requestMatchers("/api/v1/profiles/**").permitAll()

                                                // All other endpoints require authentication
                                                .anyRequest().authenticated())
                                // Stateless token-based auth
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
