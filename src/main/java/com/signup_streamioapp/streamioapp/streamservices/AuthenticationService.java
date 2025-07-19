package com.signup_streamioapp.streamioapp.streamservices;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.signup_streamioapp.streamioapp.AuthenticationResponse;
import com.signup_streamioapp.streamioapp.streamcontroller.RegisterRequest;
import com.signup_streamioapp.streamioapp.streammodels.Role;
import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streamrepository.UserRepository;
import com.signup_streamioapp.streamioapp.streamrequest.AuthenticationRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;

        private String normalizeEmail(String email) {
                return email.trim().toLowerCase();
        }

        public AuthenticationResponse register(RegisterRequest request) {
                String email = normalizeEmail(request.getEmail());

                if (!request.getPassword().equals(request.getConfirmPassword())) {
                        throw new IllegalArgumentException("Passwords do not match");
                }

                var existingUser = userRepository.findByEmailIgnoreCase(email);

                if (existingUser.isPresent()) {
                        User user = existingUser.get();

                        if (!user.isEnabled()) {
                                String token = jwtService.generateToken(user);
                                System.out.println("🔁 Resending confirmation email to: " + user.getEmail());
                                emailService.sendConfirmationEmail(user.getEmail(), token);
                                return AuthenticationResponse.builder()
                                                .token(token)
                                                .build();
                        }

                        throw new IllegalArgumentException("Email already registered");
                }

                var user = User.builder()
                                .email(email)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .enabled(false)
                                .build();

                userRepository.save(user);

                String token = jwtService.generateToken(user);
                System.out.println("📨 Sending confirmation email to: " + user.getEmail());
                emailService.sendConfirmationEmail(user.getEmail(), token);

                return AuthenticationResponse.builder()
                                .token(token)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                String email = normalizeEmail(request.getEmail());

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(email, request.getPassword()));

                var user = userRepository.findByEmailIgnoreCase(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                if (!user.isEnabled()) {
                        throw new RuntimeException("Account not confirmed");
                }

                var jwtToken = jwtService.generateToken(user);

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public void sendResetLink(String email) {
                forgotPassword(email);
        }

        public void forgotPassword(String email) {
                String normalizedEmail = normalizeEmail(email);

                var user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtService.generateToken(user);

                System.out.println("🔐 Reset password token: " + token);

                emailService.sendResetPasswordEmail(user.getEmail(), token);
        }

        public void resetPassword(String token, String newPassword) {
                String email = normalizeEmail(jwtService.extractUsername(token));

                var user = userRepository.findByEmailIgnoreCase(email)
                                .orElseThrow(() -> new RuntimeException("Invalid token"));

                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
        }
}
