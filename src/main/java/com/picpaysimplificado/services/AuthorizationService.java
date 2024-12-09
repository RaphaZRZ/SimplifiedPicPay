package com.picpaysimplificado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate; // Fazer requisições HTTP entre serviços


    // Verificação da autorização da transação com base numa API interna
    public boolean transactionIsAuthorized() {
        String internalApiURL = "http://localhost:8080/authorize-transaction-api";

        ResponseEntity<Boolean> authorizationResponse = this.restTemplate.getForEntity(internalApiURL, Boolean.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) // Se a API funcionar corretamente
            return authorizationResponse.getBody();
        return false; // Retorna falso se ocorrer algum erro
    }
}
