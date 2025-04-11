package com.example.auth.service;

import com.example.auth.model.Occurrence;
import com.example.auth.model.User;
import com.example.auth.model.Verification;
import com.example.auth.repository.OccurrenceRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OccurrenceService {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    @Qualifier("exampleUserRepository")
    private UserRepository userRepository;
    
    @Autowired
    @Qualifier("userRepository")
    private com.auth.repository.UserRepository mainUserRepository;

    @Autowired
    private VerificationRepository verificationRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Occurrence registerOccurrence(String title, String description, String type, 
                                       double latitude, double longitude, 
                                       String district,
                                       MultipartFile photo,
                                       String username) {
        // Using the main UserRepository to check if the user exists
        Optional<com.auth.model.User> mainUserOpt = mainUserRepository.findByUsername(username);
        if (mainUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        // Convert to our local User model or find it in our repository
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            // Create a new User instance from the main User model
            User newUser = new User();
            newUser.setUsername(username);
            // Save any other necessary fields
            userOpt = Optional.of(userRepository.save(newUser));
        }

        // Handle photo upload and get URL
        String photoUrl = handlePhotoUpload(photo);

        Occurrence occurrence = new Occurrence();
        occurrence.setTitle(title);
        occurrence.setDescription(description);
        occurrence.setLatitude(latitude);
        occurrence.setLongitude(longitude);
        occurrence.setTimestamp(LocalDateTime.now());
        occurrence.setCreatedAt(LocalDateTime.now());
        occurrence.setUser(userOpt.get());
        occurrence.setType(type);
        occurrence.setPhotoUrl(photoUrl);
        occurrence.setDistrict(district);

        // Save the occurrence
        occurrence = occurrenceRepository.save(occurrence);

        // Create verification request
        Verification verification = new Verification();
        verification.setLocation(String.format("%.6f, %.6f", latitude, longitude));
        verification.setType(type);
        verification.setSubmittedBy(username);
        verification.setDate(LocalDateTime.now());
        verification.setStatus("PENDING");
        verification.setOccurrenceId(occurrence.getId());
        verification.setDistrict(district);

        verificationRepository.save(verification);
        
        // Create notification for admins
        notificationService.notifyAdminsAboutNewOccurrence(occurrence);

        return occurrence;
    }

    private String handlePhotoUpload(MultipartFile photo) {
        // TODO: Implement photo upload logic
        return "placeholder_url";
    }

    public List<Occurrence> getAllOccurrences() {
        return occurrenceRepository.findAll();
    }

    public List<Occurrence> getOccurrencesByUser(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return occurrenceRepository.findByUser(userOpt.get());
    }

    public Optional<Occurrence> getOccurrenceById(String id) {
        return occurrenceRepository.findById(id);
    }

    /**
     * Get all approved occurrences
     * 
     * @return List of approved occurrences
     */
    public List<Occurrence> getApprovedOccurrences() {
        return occurrenceRepository.findByStatus("APPROVED");
    }

    /**
     * Update the status of an occurrence
     * 
     * @param id The occurrence ID
     * @param status The new status (APPROVED, REJECTED)
     * @param adminNotes Notes from the admin
     * @param adminUsername The username of the admin making the update
     * @return The updated occurrence
     */
    @Transactional
    public Occurrence updateOccurrenceStatus(String id, String status, String adminNotes, String adminUsername) {
        Occurrence occurrence = occurrenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Occurrence not found"));
        
        // Update the occurrence status
        occurrence.setStatus(status);
        occurrence.setAdminNotes(adminNotes);
        
        // Save the updated occurrence
        occurrence = occurrenceRepository.save(occurrence);
        
        // Update the corresponding verification if it exists
        verificationRepository.findByOccurrenceId(id).ifPresent(verification -> {
            verification.setStatus(status);
            verification.setVerifiedBy(adminUsername);
            verification.setVerifiedAt(LocalDateTime.now());
            
            verificationRepository.save(verification);
        });
        
        return occurrence;
    }
} 