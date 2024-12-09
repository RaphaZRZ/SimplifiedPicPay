package com.picpaysimplificado.services;

import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
import com.picpaysimplificado.exceptions.UnauthorizedTransactionException;
import com.picpaysimplificado.models.Transaction;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.models.Usuario;
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
    private UsuarioService usuarioService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthorizationService authorizationService;


    public Transaction createTransaction(TransactionDTO transactionData) throws Exception {
        Usuario sender = this.usuarioService.findUsuarioById(transactionData.senderId());
        Usuario receiver = this.usuarioService.findUsuarioById(transactionData.receiverId());

        this.usuarioService.validateTransaction(sender, transactionData.amount());

        // Verifica se a transação está autorizada, caso esteja, cria a transação
        boolean isAuthorized = this.authorizationService.transactionIsAuthorized();
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
}
