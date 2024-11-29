package com.picpaysimplificado.services;

import com.picpaysimplificado.exceptions.UnauthorizedTransactionException;
import com.picpaysimplificado.models.Transaction;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate; // Fazer requisições HTTP entre serviços


    public Transaction createTransaction(TransactionDTO transactionData) throws Exception {
        Usuario sender = this.usuarioService.findUsuarioById(transactionData.senderId());
        Usuario receiver = this.usuarioService.findUsuarioById(transactionData.receiverId());

        this.usuarioService.validateTransaction(sender, transactionData.amount());

        boolean isAuthorized = this.authorizeTransaction(sender, transactionData.amount());
        if (!isAuthorized)
            throw new UnauthorizedTransactionException();

        // Criando transação
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionData.amount());
        transaction.setSenderId(sender);
        transaction.setReceiverId(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        // Atualizando saldo dos usuários
        sender.setBalance(sender.getBalance().subtract(transactionData.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionData.amount()));

        // Salvando transação e saldo os usuários
        this.transactionRepository.save(transaction);
        this.usuarioService.saveUsuario(sender);
        this.usuarioService.saveUsuario(receiver);

        // Notificações para usuários
        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");


        return transaction;
    }

    // Verificação com base numa API interna
    public boolean authorizeTransaction(Usuario sender, BigDecimal amount) {
        String internalApiURL = "http://localhost:8080/authorize-transaction-api";

        ResponseEntity<Boolean> authorizationResponse = restTemplate.getForEntity(internalApiURL, Boolean.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK)
            return authorizationResponse.getBody();
        return false; // Retorna falso se ocorrer algum erro
    }
}
