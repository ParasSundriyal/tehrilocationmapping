package com.auth.service;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    public void sendEmail(String to, String subject, String body) {
        // TODO: Implement email sending logic
        // This could use JavaMailSender, SendGrid, or other email services
        System.out.println("Email notification sent to: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
}
