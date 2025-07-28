package com.signup_streamioapp.streamioapp.streamservices;

import com.signup_streamioapp.streamioapp.ConfirmationToken;
import com.signup_streamioapp.streamioapp.streammodels.TokenType;
import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streamrepository.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String generateConfirmationToken(User user, TokenType tokenType) {
        String token = generateSixDigitOTP(); // ✅ Use 6-digit numeric OTP

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                tokenType,
                user);

        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    // ✅ Generates a random 6-digit number between 100000 and 999999
    private String generateSixDigitOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        System.out.println("🔍 Looking for token: " + token);
        System.out.println("❓Token found? " + (confirmationToken != null));
        System.out.println("⏱️ ExpiresAt: " + confirmationToken.getExpiresAt());
        System.out.println("⏱️ Now: " + LocalDateTime.now());

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already used");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());

        if (confirmationToken.getTokenType() == TokenType.VERIFY_EMAIL) {
            confirmationToken.getUser().setEnabled(true);
        }

        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getValidToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already used");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        return confirmationToken;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
}
