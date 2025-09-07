package com.auth.service;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppNotificationService {

    public void sendWhatsApp(String phoneNumber, String message) {
        // TODO: Implement WhatsApp sending logic
        // This could use WhatsApp Business API, Twilio WhatsApp, or other services
        System.out.println("WhatsApp notification sent to: " + phoneNumber);
        System.out.println("Message: " + message);
    }
}
