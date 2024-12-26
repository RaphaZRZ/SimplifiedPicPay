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

        // Verify if the transaction is authorized
        boolean isAuthorized = this.authorizationService.transactionIsAuthorized();
        if (!isAuthorized)
            throw new UnauthorizedTransactionException();
        Transaction transaction = buildTransaction(transactionData.amount(), sender, receiver);

        // Update users' balances
        sender.setBalance(sender.getBalance().subtract(transactionData.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionData.amount()));
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        // Verify if the notification system is online, if not, reverse the transaction
        boolean notifyUserIsOnline = this.notificationService.notifyUserIsOnline();
        if (!notifyUserIsOnline) {
            rollbackTransaction(transactionData.amount(), sender, receiver);
            throw new NotificationServiceOfflineException();
        }

        // Notify users and save the transaction
        this.notificationService.sendNotification(sender, "Transaction completed successfully.");
        this.notificationService.sendNotification(receiver, "Transaction received successfully.");
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
