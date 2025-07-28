package com.signup_streamioapp.streamioapp.streamservices;

import com.signup_streamioapp.streamioapp.AuthenticationResponse;
import com.signup_streamioapp.streamioapp.streamcontroller.RegisterRequest;
import com.signup_streamioapp.streamioapp.streammodels.Role;
import com.signup_streamioapp.streamioapp.streammodels.TokenType;
import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streamrepository.UserRepository;
import com.signup_streamioapp.streamioapp.streamrequest.AuthenticationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    // Normalize email (lowercase and trimmed)
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    // Basic password validation rule
    private boolean isPasswordStrong(String password) {
        // At least 8 characters, 1 digit, 1 upper, 1 lower, 1 special char
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(pattern, password);
    }

    // Register a new user
    public AuthenticationResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (!isPasswordStrong(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include an uppercase letter, number, and special character");
        }

        var existingUser = userRepository.findByEmailIgnoreCase(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (!user.isEnabled()) {
                String token = jwtService.generateToken(user);
                System.out.println("🔁 Resending confirmation email to: " + user.getEmail());
                emailService.sendConfirmationEmail(user.getEmail(), token);
                return AuthenticationResponse.builder().token(token).build();
            }

            throw new IllegalArgumentException("Email already registered");
        }

        var user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false) // initially disabled until confirmed
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        System.out.println("📨 Sending confirmation email to: " + user.getEmail());
        emailService.sendConfirmationEmail(user.getEmail(), token);

        return AuthenticationResponse.builder().token(token).build();
    }

    // Login authentication
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = normalizeEmail(request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password"));

        if (!user.isEnabled()) {
            throw new IllegalStateException("Please verify your email before logging in.");
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    // Public method to initiate password reset via OTP
    public void sendResetLink(String email) {
        forgotPassword(email);
    }

    // Internal password reset OTP generation logic
    public void forgotPassword(String email) {
        String normalizedEmail = normalizeEmail(email);

        var user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = confirmationTokenService.generateConfirmationToken(user, TokenType.RESET_PASSWORD);
        String message = "Your OTP is: " + otp + "\nIt expires in 15 minutes.";

        emailService.sendMail(user.getEmail(), "Reset Password OTP", message);
        System.out.println("✅ OTP sent to: " + user.getEmail());
    }

    // Final password reset with OTP and new password
    public void resetPassword(String otp, String newPassword) {
        System.out.println("🚨 RESET PASSWORD REQUEST");
        System.out.println("📥 Received OTP: " + otp);
        System.out.println("🕒 Now: " + LocalDateTime.now());

        var confirmationToken = confirmationTokenService.getValidToken(otp);

        System.out.println("🔐 Token found for user: " + confirmationToken.getUser().getEmail());
        System.out.println("⏱️ Token expires at: " + confirmationToken.getExpiresAt());

        User user = confirmationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEnabled(true); // ✅ Fix: enable user after successful password reset
        userRepository.save(user);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        System.out.println("✅ Password reset successful for: " + user.getEmail());
    }
}
