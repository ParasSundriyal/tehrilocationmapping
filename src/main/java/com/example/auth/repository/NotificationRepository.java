package com.example.auth.repository;

import com.example.auth.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    // Find notifications for a specific recipient
    List<Notification> findByRecipientIdOrderByTimestampDesc(String recipientId);
    
    // Find unread notifications for a specific recipient
    List<Notification> findByRecipientIdAndReadIsFalseOrderByTimestampDesc(String recipientId);
    
    // Count unread notifications for a specific recipient
    long countByRecipientIdAndReadIsFalse(String recipientId);
} 