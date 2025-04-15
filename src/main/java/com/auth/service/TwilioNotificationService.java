package com.auth.service;

import com.auth.config.TwilioConfig;
import com.auth.model.EmergencyContact;
import com.auth.model.Occurrence;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioNotificationService {

    @Autowired
    private TwilioConfig twilioConfig;

    public void sendSMS(String toPhoneNumber, String message) {
        try {
            Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                message
            ).create();
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            throw e;
        }
    }

    public void sendEmergencySMS(Occurrence occurrence, EmergencyContact contact) {
        String message = createEmergencyMessage(occurrence, contact);
        sendSMS(contact.getPhoneNumber(), message);
    }

    private String createEmergencyMessage(Occurrence occurrence, EmergencyContact contact) {
        return String.format(
            "🚨 EMERGENCY ALERT - %s\n\n" +
            "📍 Location: %s, %s\n" +
            "📱 Reporter: %s (%s)\n" +
            "📝 Details: %s\n\n" +
            "⚠️ Please respond immediately.\n" +
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