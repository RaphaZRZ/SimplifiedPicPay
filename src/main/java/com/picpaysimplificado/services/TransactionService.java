package com.picpaysimplificado.services;

import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
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

        // Verifica se a transação é autorizada, caso seja, cria a transação
        boolean isAuthorized = this.authorizeTransaction();
        if (!isAuthorized)
            throw new UnauthorizedTransactionException();
        Transaction transaction = buildTransaction(transactionData.amount(), sender, receiver);

        // Atualizando saldo dos usuários
        sender.setBalance(sender.getBalance().subtract(transactionData.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionData.amount()));

        // Atualizando saldo dos usuários
        this.usuarioService.saveUsuario(sender);
        this.usuarioService.saveUsuario(receiver);

        // Verificar se o sistema de notificação está online, reverter transação caso esteja offline
        boolean notifyUserIsOnline = notifyUser();
        if (!notifyUserIsOnline) {
            rollbackTransaction(transactionData.amount(), sender, receiver);
            throw new NotificationServiceOfflineException();
        }

        // Notifica os usuários e salva a transação
        this.notificationService.sendNotification(sender, "Transação realizada com sucesso.");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso.");
        this.transactionRepository.save(transaction);

        return transaction;
    }

    public Transaction buildTransaction(BigDecimal amount, Usuario sender, Usuario receiver) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }

    public void rollbackTransaction(BigDecimal amount, Usuario sender, Usuario receiver) {
        sender.setBalance(sender.getBalance().add(amount));
        receiver.setBalance(receiver.getBalance().subtract(amount));
    }

    // Verificação da autorização da transação com base numa API interna
    public boolean authorizeTransaction() {
        String internalApiURL = "http://localhost:8080/authorize-transaction-api";

        ResponseEntity<Boolean> authorizationResponse = this.restTemplate.getForEntity(internalApiURL, Boolean.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) // Se a API funcionar corretamente
            return authorizationResponse.getBody();
        return false; // Retorna falso se ocorrer algum erro
    }

    // Verifica se o serviço de envio de emails está online
    public boolean notifyUser() {
        String internalApiURL = "http://localhost:8080/notify-user-api";

        ResponseEntity<Boolean> notifyResponse = this.restTemplate.getForEntity(internalApiURL, boolean.class);

        if (notifyResponse.getStatusCode() == HttpStatus.OK) // Se a API funcionar corretamente
            return notifyResponse.getBody();
        return false;
    }
}
