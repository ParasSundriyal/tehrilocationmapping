package com.auth.service;

import com.auth.model.EmergencyContact;
import com.auth.model.Occurrence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    @Autowired(required = false)
    private TwilioNotificationService twilioService;

    @Autowired(required = false)
    private EmailNotificationService emailService;

    @Autowired(required = false)
    private WhatsAppNotificationService whatsAppService;

    @Autowired(required = false)
    private PushNotificationService pushService;

    public void sendEmergencyNotification(Occurrence occurrence, EmergencyContact contact) {
        String message = createEmergencyMessage(occurrence, contact);
        
        // Try different notification methods in order of priority
        try {
            // 1. Try SMS first (fastest)
            if (twilioService != null && contact.getPhoneNumber() != null) {
                twilioService.sendSMS(contact.getPhoneNumber(), message);
                return;
            }
            
            // 2. Try WhatsApp (good for India)
            if (whatsAppService != null && contact.getPhoneNumber() != null) {
                whatsAppService.sendWhatsApp(contact.getPhoneNumber(), message);
                return;
            }
            
            // 3. Try Email (most reliable)
            if (emailService != null && contact.getEmail() != null) {
                emailService.sendEmail(
                    contact.getEmail(),
                    "Emergency Alert: " + occurrence.getTitle(),
                    message
                );
                return;
            }
            
            // 4. Try Push Notification (if available)
            if (pushService != null && contact.getDeviceToken() != null && !contact.getDeviceToken().trim().isEmpty()) {
                pushService.sendPush(
                    contact.getDeviceToken(),
                    "Emergency Alert",
                    message
                );
            }
        } catch (Exception e) {
            // Log the error and try next method
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
