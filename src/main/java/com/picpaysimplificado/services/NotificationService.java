package com.picpaysimplificado.services;

import com.picpaysimplificado.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate; // Perform HTTP requests between services


    // Verify if the email sending service is online
    public boolean notifyUserIsOnline() {
        String internalApiURL = "http://localhost:8080/notify-user-api";

        ResponseEntity<Boolean> notifyResponse = this.restTemplate.getForEntity(internalApiURL, boolean.class);

        if (notifyResponse.getStatusCode() == HttpStatus.OK) // If the API works correctly
            return notifyResponse.getBody();
        return false;
    }

    // Simulate the notification service being offline
    public void sendNotification(User user, String message) {
        String email = user.getEmail();

        System.out.println("Notification sent to email " + email + " successfully.\n" + message);
    }
}
