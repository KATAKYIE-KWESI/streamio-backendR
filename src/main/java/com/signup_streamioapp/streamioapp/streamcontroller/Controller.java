// package com.signup_streamioapp.streamioapp.streamcontroller;

// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.signup_streamioapp.streamioapp.AuthenticationResponse;
// import
// com.signup_streamioapp.streamioapp.streamrequest.AuthenticationRequest;
// import
// com.signup_streamioapp.streamioapp.streamservices.AuthenticationService;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/auth")
// @RequiredArgsConstructor
// public class Controller {

// private final AuthenticationService authenticationService;

// @PostMapping("/authenticate")
// public ResponseEntity<AuthenticationResponse> authenticate(
// @RequestBody AuthenticationRequest request) {
// return ResponseEntity.ok(authenticationService.authenticate(request));
// }

// @PostMapping("/forgot-password")
// public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String>
// request) {
// String email = request.get("email");
// authenticationService.sendResetLink(email); // ✅ This should match your
// AuthenticationService
// return ResponseEntity.ok("Password reset link sent to your email if it
// exists.");
// }

// @PostMapping("/reset-password")
// public ResponseEntity<String> resetPassword(@RequestBody Map<String, String>
// request) {
// String token = request.get("token");
// String newPassword = request.get("newPassword");
// authenticationService.resetPassword(token, newPassword);
// return ResponseEntity.ok("Password reset successfully.");
// }
// }
