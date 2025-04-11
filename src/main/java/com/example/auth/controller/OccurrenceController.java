package com.example.auth.controller;

import com.example.auth.model.Occurrence;
import com.example.auth.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/occurrences")
@CrossOrigin(origins = "*")
public class OccurrenceController {

    @Autowired
    private OccurrenceService occurrenceService;

    @GetMapping("/test")
    public ResponseEntity<?> testConnection() {
        try {
            List<Occurrence> occurrences = occurrenceService.getAllOccurrences();
            return ResponseEntity.ok(Map.of(
                "message", "Connection successful",
                "count", occurrences.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName()
            ));
        }
    }

    @PostMapping("/upload-test")
    public ResponseEntity<?> testUpload(
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(Map.of(
                "message", "File received successfully",
                "fileName", file.getOriginalFilename(),
                "fileSize", file.getSize(),
                "contentType", file.getContentType()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> registerOccurrence(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            Authentication authentication) {
        
        try {
            String username = authentication.getName();
            Occurrence occurrence = occurrenceService.registerOccurrence(
                title, description, type, latitude, longitude, district, photo, username);
            return ResponseEntity.ok(occurrence);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getOccurrenceById(@PathVariable String id) {
        Optional<Occurrence> occurrenceOpt = occurrenceService.getOccurrenceById(id);
        
        if (occurrenceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(occurrenceOpt.get());
    }
    
    @GetMapping("/approved")
    public ResponseEntity<List<Occurrence>> getApprovedOccurrences() {
        List<Occurrence> approvedOccurrences = occurrenceService.getApprovedOccurrences();
        return ResponseEntity.ok(approvedOccurrences);
    }
    
    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateOccurrenceStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> statusUpdate,
            Authentication authentication) {
        
        try {
            String status = statusUpdate.get("status");
            String adminNotes = statusUpdate.get("adminNotes");
            
            if (status == null || !status.matches("APPROVED|REJECTED")) {
                return ResponseEntity.badRequest().body("Invalid status. Must be 'APPROVED' or 'REJECTED'");
            }
            
            String adminUsername = authentication.getName();
            Occurrence updatedOccurrence = occurrenceService.updateOccurrenceStatus(id, status, adminNotes, adminUsername);
            
            return ResponseEntity.ok(updatedOccurrence);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 