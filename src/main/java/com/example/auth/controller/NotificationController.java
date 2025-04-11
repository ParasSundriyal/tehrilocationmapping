package com.example.auth.controller;

import com.example.auth.model.Notification;
import com.example.auth.service.NotificationService;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    
    /**
     * Get all notifications for the authenticated user
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(Authentication authentication) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Notification> notifications = notificationService.getNotificationsForUser(userOpt.get().getId());
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Get unread notifications for the authenticated user
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(userOpt.get().getId());
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Mark a notification as read
     */
    @PostMapping("/{id}/mark-read")
    public ResponseEntity<?> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Mark all notifications as read for the authenticated user
     */
    @PostMapping("/mark-all-read")
    public ResponseEntity<?> markAllAsRead(Authentication authentication) {
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        notificationService.markAllAsRead(userOpt.get().getId());
        return ResponseEntity.ok().build();
    }
} 