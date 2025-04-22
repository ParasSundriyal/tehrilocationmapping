package com.auth.service;

import com.auth.model.EmergencyContact;
import com.auth.model.Occurrence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private TwilioNotificationService twilioService;

    public void sendEmergencyNotification(Occurrence occurrence, EmergencyContact contact) {
        String message = createEmergencyMessage(occurrence, contact);
        
        try {
            if (contact.getPhoneNumber() != null) {
                twilioService.sendSMS(contact.getPhoneNumber(), message);
            }
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    private String createEmergencyMessage(Occurrence occurrence, EmergencyContact contact) {
        return String.format(
            "🚨 EMERGENCY ALERT - %s\n\n" +
            "📍 Location: %s, %s\n" +
            "📱 Reporter: %s (%s)\n" +
            "📝 Details: %s\n\n" +
            "⚠ Please respond immediately.\n" +
            "🔗 View on map: https://tehrimap.com/occurrence/%s",
            occurrence.getTitle(),
            occurrence.getLocation(),
            occurrence.getDistrict(),
            occurrence.getReporterName(),
            occurrence.getReporterPhone(),
            occurrence.getDescription(),
            occurrence.getId()
        );
    }
}