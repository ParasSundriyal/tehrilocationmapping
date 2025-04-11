package com.example.auth.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "occurrences")
public class Occurrence {
    @Id
    private String id;

    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
    private String type;
    private String photoUrl;
    private String district;

    @DBRef
    private User user;

    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private String adminNotes;
} 