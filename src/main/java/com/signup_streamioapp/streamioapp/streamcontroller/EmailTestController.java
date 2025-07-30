package com.signup_streamioapp.streamioapp.streamcontroller;

import com.signup_streamioapp.streamioapp.streamservices.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
