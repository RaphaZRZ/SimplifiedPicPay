package com.picpaysimplificado.services;

import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
import com.picpaysimplificado.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.security.SecureRandom;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;


    // Simula um serviço de notificação fora do ar
    public void sendNotification(Usuario usuario, String message) throws Exception {
        int randomValue = new SecureRandom().nextInt(20);
        String email = usuario.getEmail();

        if (randomValue < 15)
            System.out.println("Notificação enviada para o email " + email +" com sucesso.\n" + message);
        else throw new NotificationServiceOfflineException();
    }
}
