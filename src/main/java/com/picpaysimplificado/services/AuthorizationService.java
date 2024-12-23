package com.picpaysimplificado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate; // Perform HTTP requests between services


    // Verify transaction authorization based on an internal API
    public boolean transactionIsAuthorized() {
        String internalApiURL = "http://localhost:8080/authorize-transaction-api";

        ResponseEntity<Boolean> authorizationResponse = this.restTemplate.getForEntity(internalApiURL, Boolean.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) // If the API works correctly
            return authorizationResponse.getBody();
        return false;
    }
}
