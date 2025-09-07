package com.auth.service;

import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    public void sendPush(String deviceToken, String title, String body) {
        // TODO: Implement push notification logic
        // This could use Firebase Cloud Messaging (FCM), Apple Push Notification Service (APNS), etc.
        System.out.println("Push notification sent to device: " + deviceToken);
        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
    }
}
