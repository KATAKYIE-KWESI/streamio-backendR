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
import com.signup_streamioapp.streamioapp.streammodels.TokenType;
import com.signup_streamioapp.streamioapp.streamrepository.UserRepository;
import com.signup_streamioapp.streamioapp.streamrequest.AuthenticationRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

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

    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword()));
    } catch (Exception e) {
        e.printStackTrace(); // Optional: logs the real issue
        throw new IllegalArgumentException("Incorrect email or password");
    }

    var user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!user.isEnabled()) {
        throw new IllegalStateException("Please verify your email before logging in.");
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

        // ✅ FIXED: Pass TokenType.RESET_PASSWORD
        String otp = confirmationTokenService.generateConfirmationToken(user, TokenType.RESET_PASSWORD);
        String message = "Your OTP is: " + otp + "\nIt expires in 15 minutes.";

        emailService.sendMail(user.getEmail(), "Reset Password OTP", message);
        System.out.println("✅ OTP sent to: " + user.getEmail());
    }

    public void resetPassword(String otp, String newPassword) {
    System.out.println("🚨 RESET PASSWORD REQUEST");
    System.out.println("📥 Received OTP: " + otp);
    System.out.println("🕒 Now: " + LocalDateTime.now());

    var confirmationToken = confirmationTokenService.getValidToken(otp);

    System.out.println("🔐 Token found for user: " + confirmationToken.getUser().getEmail());
    System.out.println("⏱️ Token expires at: " + confirmationToken.getExpiresAt());

    User user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    confirmationToken.setConfirmedAt(LocalDateTime.now());
    confirmationTokenService.saveConfirmationToken(confirmationToken);

    System.out.println("✅ Password reset successful for: " + user.getEmail());
    }
}
