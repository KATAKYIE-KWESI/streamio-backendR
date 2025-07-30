package com.signup_streamioapp.streamioapp.streamcontroller;

import com.signup_streamioapp.streamioapp.AuthenticationResponse;
import com.signup_streamioapp.streamioapp.streamrequest.AuthenticationRequest;
import com.signup_streamioapp.streamioapp.streamrequest.ForgotPasswordRequest;

import com.signup_streamioapp.streamioapp.streamrequest.ResetPasswordRequest;
import com.signup_streamioapp.streamioapp.streamservices.AuthenticationService;
import com.signup_streamioapp.streamioapp.streamservices.EmailService;
import com.signup_streamioapp.streamioapp.streamservices.SubscriptionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final SubscriptionService subscriptionService; // ✅ Add this line

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Incorrect email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An unexpected error occurred"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authenticationService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(Collections.singletonMap("message", "OTP sent to: " + request.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authenticationService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Collections.singletonMap("message", "Password reset successful"));

    }

    @GetMapping("/cinemas")
    public ResponseEntity<?> getPartnerCinemas(@RequestParam String email) {
        try {
            return ResponseEntity.ok(subscriptionService.getPartnerCinemas(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/ticket")
    public ResponseEntity<?> getDiscountTicket(@RequestParam String email) {
        try {
            return ResponseEntity
                    .ok(Collections.singletonMap("ticketCode", subscriptionService.getDiscountTicketCode(email)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @RestController
    @RequestMapping("/api/v1/test-email")
    @RequiredArgsConstructor
    public class EmailTestController {

        private final EmailService emailService;

        @GetMapping("/send")
        public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
            emailService.sendMail(to, "Test Email from Streamio", "<h1>This is a test email via SendGrid</h1>");
            return ResponseEntity.ok("Email sent to: " + to);
        }
    }

}
