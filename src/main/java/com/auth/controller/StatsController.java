package com.auth.controller;

import com.auth.model.MapMarker;
import com.auth.model.User;
import com.auth.repository.MapMarkerRepository;
import com.auth.repository.UserRepository;
import com.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StatsController {
    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapMarkerRepository markerRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            logger.info("Received request for reports");
            
            // Check if token exists
            if (token == null) {
                logger.error("No token provided");
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: No token provided"));
            }
            
            // Check if token is in the correct format
            if (!token.startsWith("Bearer ")) {
                logger.error("Token doesn't start with Bearer");
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: Invalid token format"));
            }
            
            // Extract JWT token
            String jwt = token.substring(7);
            logger.debug("JWT token length: {}", jwt.length());
            
            try {
                // Extract username from token
                String username = jwtUtil.extractUsername(jwt);
                logger.info("Username extracted from token: {}", username);
                
                if (username == null) {
                    logger.error("Username is null in token");
                    return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: Invalid token"));
                }
                
                // Check if user exists and has proper permissions
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isEmpty()) {
                    logger.error("User not found: {}", username);
                    return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: User not found"));
                }
                
                User currentUser = userOpt.get();
                logger.info("User found: {}, role: {}", username, currentUser.getRole());
                
                if (!currentUser.isAdmin() && !currentUser.isSuperAdmin()) {
                    logger.error("User doesn't have admin permissions: {}", username);
                    return ResponseEntity.status(403).body(Map.of("message", "Forbidden: Admin access required"));
                }
                
                Map<String, Object> response = new HashMap<>();
                
                // Get user statistics
                List<Map<String, Object>> userStats = getUserStatistics();
                response.put("userStats", userStats);
                logger.info("User stats collected: {} entries", userStats.size());
                
                // Get marker statistics
                List<Map<String, Object>> markerStats = getMarkerStatistics();
                response.put("markerStats", markerStats);
                logger.info("Marker stats collected: {} entries", markerStats.size());
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("Error processing JWT token", e);
                return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: " + e.getMessage()));
            }
        } catch (Exception e) {
            logger.error("Error getting reports", e);
            return ResponseEntity.badRequest().body(Map.of("message", "Error getting reports: " + e.getMessage()));
        }
    }
    
    private List<Map<String, Object>> getUserStatistics() {
        List<User> users = userRepository.findAll();
        List<MapMarker> allMarkers = markerRepository.findAll();
        
        return users.stream()
            .filter(user -> "ADMIN".equals(user.getRole()) || "SUPERADMIN".equals(user.getRole()))
            .map(user -> {
                Map<String, Object> userStat = new HashMap<>();
                userStat.put("username", user.getUsername());
                
                // Count markers created by this user
                List<MapMarker> userMarkers = allMarkers.stream()
                    .filter(marker -> marker.getCreatedBy() != null && marker.getCreatedBy().equals(user.getId()))
                    .collect(Collectors.toList());
                
                userStat.put("totalMarkers", userMarkers.size());
                
                long activeMarkers = userMarkers.stream()
                    .filter(MapMarker::isEnabled)
                    .count();
                    
                userStat.put("activeMarkers", activeMarkers);
                userStat.put("inactiveMarkers", userMarkers.size() - activeMarkers);
                
                return userStat;
            })
            .collect(Collectors.toList());
    }
    
    private List<Map<String, Object>> getMarkerStatistics() {
        List<MapMarker> allMarkers = markerRepository.findAll();
        
        // Group markers by type
        Map<String, List<MapMarker>> markersByType = allMarkers.stream()
            .collect(Collectors.groupingBy(
                marker -> marker.getMarkerType() != null ? marker.getMarkerType() : "UNKNOWN"
            ));
            
        return markersByType.entrySet().stream()
            .map(entry -> {
                Map<String, Object> markerStat = new HashMap<>();
                markerStat.put("type", entry.getKey());
                
                List<MapMarker> markers = entry.getValue();
                markerStat.put("count", markers.size());
                
                long activeCount = markers.stream()
                    .filter(MapMarker::isEnabled)
                    .count();
                    
                markerStat.put("activeCount", activeCount);
                markerStat.put("inactiveCount", markers.size() - activeCount);
                
                return markerStat;
            })
            .collect(Collectors.toList());
    }
} 