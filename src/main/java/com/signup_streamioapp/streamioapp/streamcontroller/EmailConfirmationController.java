package com.signup_streamioapp.streamioapp.streamcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.signup_streamioapp.streamioapp.streamrepository.UserRepository;
import com.signup_streamioapp.streamioapp.streamservices.JwtService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmailConfirmationController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        try {
            // Normalize extracted email
            String email = jwtService.extractUsername(token).trim().toLowerCase();

            return userRepository.findByEmailIgnoreCase(email)
                    .map(user -> {
                        user.setEnabled(true);
                        userRepository.save(user);
                        return ResponseEntity.ok("Account confirmed successfully.");
                    })
                    .orElse(ResponseEntity.badRequest().body("Invalid token or user not found."));
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Enter a different email or expired token.");
        }
    }
}
