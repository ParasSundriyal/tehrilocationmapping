package com.example.auth.service;

import com.example.auth.model.Notification;
import com.example.auth.model.Occurrence;
import com.example.auth.repository.NotificationRepository;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    
    /**
     * Create a notification for a new occurrence
     */
    public void notifyAdminsAboutNewOccurrence(Occurrence occurrence) {
        // Find all admin users
        List<User> admins = userRepository.findAll().stream()
                .filter(user -> "ADMIN".equals(user.getRole()) || "SUPERADMIN".equals(user.getRole()))
                .collect(Collectors.toList());
        
        // Create notification for each admin
        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setTitle("New Occurrence Submission");
            
            String message = String.format("A new %s occurrence has been submitted", 
                    occurrence.getType() != null ? occurrence.getType().toLowerCase() : "");
            
            if (occurrence.getDistrict() != null && !occurrence.getDistrict().isEmpty()) {
                message += String.format(" in %s district", occurrence.getDistrict());
            }
            
            notification.setMessage(message);
            notification.setRecipientId(admin.getId());
            notification.setType("NEW_OCCURRENCE");
            notification.setEntityId(occurrence.getId());
            notification.setTimestamp(LocalDateTime.now());
            notification.setRead(false);
            
            notificationRepository.save(notification);
        }
    }
    
    /**
     * Get notifications for a user
     */
    public List<Notification> getNotificationsForUser(String userId) {
        return notificationRepository.findByRecipientIdOrderByTimestampDesc(userId);
    }
    
    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotificationsForUser(String userId) {
        return notificationRepository.findByRecipientIdAndReadIsFalseOrderByTimestampDesc(userId);
    }
    
    /**
     * Mark a notification as read
     */
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    
    /**
     * Mark all notifications as read for a user
     */
    public void markAllAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findByRecipientIdAndReadIsFalseOrderByTimestampDesc(userId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
} 