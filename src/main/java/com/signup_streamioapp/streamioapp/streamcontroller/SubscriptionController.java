package com.signup_streamioapp.streamioapp.streamcontroller;

import com.signup_streamioapp.streamioapp.streamservices.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/start-free-trial")
    public ResponseEntity<String> startFreeTrial(@RequestParam String email) {
        try {
            subscriptionService.startFreeTrial(email);
            return ResponseEntity.ok("✅ Free trial started for 1 day.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @PostMapping("/fake-payment")
    public ResponseEntity<String> simulatePayment(@RequestParam String email) {
        subscriptionService.simulatePremiumPayment(email);
        return ResponseEntity.ok("✅ Fake premium subscription activated for 30 days.");
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkStatus(@RequestParam String email) {
        boolean isActive = subscriptionService.isSubscriptionActive(email);
        return ResponseEntity.ok(isActive);
    }
}
