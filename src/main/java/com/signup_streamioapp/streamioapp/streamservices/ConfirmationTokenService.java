package com.signup_streamioapp.streamioapp.streamservices;


import com.signup_streamioapp.streamioapp.ConfirmationToken;
import com.signup_streamioapp.streamioapp.streammodels.User;
import com.signup_streamioapp.streamioapp.streamrepository.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String generateConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.getUser().setEnabled(true);
        confirmationTokenRepository.save(confirmationToken);
    }
}
