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
    private RestTemplate restTemplate; // Fazer requisições HTTP entre serviços


    // Verifica se o serviço de envio de emails está online
    public boolean notifyUserIsOnline() {
        String internalApiURL = "http://localhost:8080/notify-user-api";

        ResponseEntity<Boolean> notifyResponse = this.restTemplate.getForEntity(internalApiURL, boolean.class);

        if (notifyResponse.getStatusCode() == HttpStatus.OK) // Se a API funcionar corretamente
            return notifyResponse.getBody();
        return false;
    }

    // Simula um serviço de notificação fora do ar
    public void sendNotification(User user, String message) {
        String email = user.getEmail();

        System.out.println("Notificação enviada para o email " + email + " com sucesso.\n" + message);
    }
}
