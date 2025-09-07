package com.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "emergency_contacts")
public class EmergencyContact {
    
    @Id
    private String id;
    
    private String name;
    private String phoneNumber;
    private String serviceType; // e.g., "Police", "Fire", "Ambulance"
    private String district;
    private boolean isActive = true;
    private String description;
    
    // Additional fields for contact details
    private String email;
    private String address;
    private String notes;
    private String deviceToken; // For push notifications
} 