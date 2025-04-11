package com.example.auth.controller;

import com.example.auth.model.Verification;
import com.example.auth.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/verifications")
@CrossOrigin(origins = "*")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingVerifications(Authentication authentication) {
        try {
            List<Verification> verifications = verificationService.getPendingVerifications();
            return ResponseEntity.ok(verifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to fetch pending verifications: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveVerification(@PathVariable String id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Verification verification = verificationService.approveVerification(id, username);
            return ResponseEntity.ok(verification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to approve verification: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectVerification(
            @PathVariable String id,
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            String reason = request.getOrDefault("reason", "No reason provided");
            Verification verification = verificationService.rejectVerification(id, username, reason);
            return ResponseEntity.ok(verification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to reject verification: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getVerificationsByUser(@PathVariable String username) {
        try {
            List<Verification> verifications = verificationService.getVerificationsByUser(username);
            return ResponseEntity.ok(verifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to fetch user verifications: " + e.getMessage()));
        }
    }
} 