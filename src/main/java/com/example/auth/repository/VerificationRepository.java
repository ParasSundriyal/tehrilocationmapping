package com.example.auth.repository;

import com.example.auth.model.Verification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationRepository extends MongoRepository<Verification, String> {
    List<Verification> findByStatus(String status);
    List<Verification> findBySubmittedBy(String username);
    List<Verification> findByVerifiedBy(String username);
    Optional<Verification> findByOccurrenceId(String occurrenceId);
} 