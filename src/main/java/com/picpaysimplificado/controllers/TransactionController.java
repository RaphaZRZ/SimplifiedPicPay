package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.models.Transaction;
import com.picpaysimplificado.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionData) throws Exception {
        Transaction transaction = this.transactionService.createTransaction(transactionData);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
