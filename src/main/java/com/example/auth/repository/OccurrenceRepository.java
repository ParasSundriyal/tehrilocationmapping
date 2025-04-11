package com.example.auth.repository;

import com.example.auth.model.Occurrence;
import com.example.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccurrenceRepository extends MongoRepository<Occurrence, String> {
    List<Occurrence> findByUser(User user);
    List<Occurrence> findByStatus(String status);
} 