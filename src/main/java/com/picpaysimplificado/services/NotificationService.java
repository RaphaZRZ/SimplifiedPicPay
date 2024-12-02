package com.picpaysimplificado.services;

import com.picpaysimplificado.models.Usuario;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Simula um serviço de notificação fora do ar
    public void sendNotification(Usuario usuario, String message) {
        String email = usuario.getEmail();

        System.out.println("Notificação enviada para o email " + email + " com sucesso.\n" + message);
    }
}
