package com.picpaysimplificado.services;

import com.picpaysimplificado.model.Transaction;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.model.Usuario;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RestTemplate restTemplate; // Fazer requisições HTTP entre serviços


    public void createTransaction(TransactionDTO transaction) throws Exception {
        Usuario sender = this.usuarioService.findUsuarioById(transaction.senderId());
        Usuario receiver = this.usuarioService.findUsuarioById(transaction.receiverId());

        this.usuarioService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized)
            throw new Exception("Transação não autorizada.");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.usuarioService.saveUsuario(sender);
        this.usuarioService.saveUsuario(receiver);
    }

    public boolean authorizeTransaction(Usuario sender, BigDecimal amount) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }
        return false;
    }
}
