package com.picpaysimplificado.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify-user-api")
public class notifyUserAPI {
    @GetMapping
    public ResponseEntity<Boolean> notificationSended() {
        // simula a lógica do consumo de uma API
        boolean isSent = Math.random() > 0.2; // 80% de chance de ser notificado

        return ResponseEntity.ok(isSent);
    }
}
