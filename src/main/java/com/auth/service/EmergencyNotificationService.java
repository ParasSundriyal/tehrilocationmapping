package com.auth.service;

import com.auth.model.EmergencyContact;
import com.auth.model.Occurrence;
import com.auth.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyNotificationService {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Autowired
    private TwilioNotificationService twilioNotificationService;

    public void notifyEmergencyContacts(Occurrence occurrence) {
        List<EmergencyContact> relevantContacts = getRelevantEmergencyContacts(occurrence);
        
        for (EmergencyContact contact : relevantContacts) {
            try {
                twilioNotificationService.sendEmergencySMS(occurrence, contact);
            } catch (Exception e) {
                System.err.println("Failed to notify emergency contact: " + contact.getName() + 
                                 " Error: " + e.getMessage());
            }
        }
    }

    private List<EmergencyContact> getRelevantEmergencyContacts(Occurrence occurrence) {
        return emergencyContactRepository.findByDistrictAndIsActive(occurrence.getDistrict(), true);
    }
} 