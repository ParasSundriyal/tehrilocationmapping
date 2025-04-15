package com.auth.controller;

import com.auth.model.EmergencyContact;
import com.auth.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @PostMapping
    public ResponseEntity<EmergencyContact> createEmergencyContact(@RequestBody EmergencyContact contact) {
        return ResponseEntity.ok(emergencyContactService.createEmergencyContact(contact));
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContact>> getAllEmergencyContacts() {
        return ResponseEntity.ok(emergencyContactService.getAllEmergencyContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable String id) {
        return emergencyContactService.getEmergencyContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContact> updateEmergencyContact(
            @PathVariable String id,
            @RequestBody EmergencyContact updatedContact) {
        EmergencyContact contact = emergencyContactService.updateEmergencyContact(id, updatedContact);
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable String id) {
        emergencyContactService.deleteEmergencyContact(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<List<EmergencyContact>> getActiveContactsByDistrict(@PathVariable String district) {
        return ResponseEntity.ok(emergencyContactService.getActiveContactsByDistrict(district));
    }

    @GetMapping("/service-type/{serviceType}")
    public ResponseEntity<List<EmergencyContact>> getActiveContactsByServiceType(@PathVariable String serviceType) {
        return ResponseEntity.ok(emergencyContactService.getActiveContactsByServiceType(serviceType));
    }

    @GetMapping("/district/{district}/service-type/{serviceType}")
    public ResponseEntity<List<EmergencyContact>> getActiveContactsByDistrictAndServiceType(
            @PathVariable String district,
            @PathVariable String serviceType) {
        return ResponseEntity.ok(emergencyContactService.getActiveContactsByDistrictAndServiceType(district, serviceType));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateContact(@PathVariable String id) {
        emergencyContactService.deactivateContact(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateContact(@PathVariable String id) {
        emergencyContactService.activateContact(id);
        return ResponseEntity.ok().build();
    }
} 