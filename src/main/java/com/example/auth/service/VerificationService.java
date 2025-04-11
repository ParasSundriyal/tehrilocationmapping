package com.example.auth.service;

import com.example.auth.model.Verification;
import com.example.auth.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationService {

    @Autowired
    private VerificationRepository verificationRepository;

    public List<Verification> getPendingVerifications() {
        return verificationRepository.findByStatus("PENDING");
    }

    public List<Verification> getVerificationsByUser(String username) {
        return verificationRepository.findBySubmittedBy(username);
    }

    @Transactional
    public Verification approveVerification(String id, String verifiedBy) {
        Optional<Verification> verificationOpt = verificationRepository.findById(id);
        if (verificationOpt.isEmpty()) {
            throw new RuntimeException("Verification not found");
        }

        Verification verification = verificationOpt.get();
        verification.setStatus("APPROVED");
        verification.setVerifiedBy(verifiedBy);
        verification.setVerifiedAt(LocalDateTime.now());

        return verificationRepository.save(verification);
    }

    @Transactional
    public Verification rejectVerification(String id, String verifiedBy, String reason) {
        Optional<Verification> verificationOpt = verificationRepository.findById(id);
        if (verificationOpt.isEmpty()) {
            throw new RuntimeException("Verification not found");
        }

        Verification verification = verificationOpt.get();
        verification.setStatus("REJECTED");
        verification.setVerifiedBy(verifiedBy);
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setRejectionReason(reason);

        return verificationRepository.save(verification);
    }

    public Optional<Verification> getVerificationById(String id) {
        return verificationRepository.findById(id);
    }
} 