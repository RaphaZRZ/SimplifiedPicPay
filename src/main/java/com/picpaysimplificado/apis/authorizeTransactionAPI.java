package com.picpaysimplificado.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize-transaction-api")
public class authorizeTransactionAPI {
    @GetMapping
    public ResponseEntity<Boolean> authorize() {
        // simula a lógica do consumo de uma API
        boolean isAuthorized = Math.random() > 0.3; // 70% de chance de retornar autorizado

        return ResponseEntity.ok(isAuthorized);
    }
}
