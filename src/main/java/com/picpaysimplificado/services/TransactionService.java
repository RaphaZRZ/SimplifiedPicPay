package com.picpaysimplificado.services;

import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
import com.picpaysimplificado.exceptions.UnauthorizedTransactionException;
import com.picpaysimplificado.models.Transaction;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthorizationService authorizationService;


    public Transaction createTransaction(TransactionDTO transactionData) throws Exception {
        User sender = this.userService.findUserById(transactionData.senderId());
        User receiver = this.userService.findUserById(transactionData.receiverId());

        this.userService.validateTransaction(sender, transactionData.amount());

        // Verifica se a transação está autorizada, caso esteja, cria a transação
        boolean isAuthorized = this.authorizationService.transactionIsAuthorized();
        if (!isAuthorized)
            throw new UnauthorizedTransactionException();
        Transaction transaction = buildTransaction(transactionData.amount(), sender, receiver);

        // Atualizando saldo dos usuários
        sender.setBalance(sender.getBalance().subtract(transactionData.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionData.amount()));

        // Atualizando saldo dos usuários
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        // Verificar se o sistema de notificação está online, reverter transação caso esteja offline
        boolean notifyUserIsOnline = this.notificationService.notifyUserIsOnline();
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

    public Transaction buildTransaction(BigDecimal amount, User sender, User receiver) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        return transaction;
    }

    public void rollbackTransaction(BigDecimal amount, User sender, User receiver) {
        sender.setBalance(sender.getBalance().add(amount));
        receiver.setBalance(receiver.getBalance().subtract(amount));
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }
}
