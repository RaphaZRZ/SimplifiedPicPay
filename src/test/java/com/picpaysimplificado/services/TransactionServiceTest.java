package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
import com.picpaysimplificado.exceptions.UnauthorizedTransactionException;
import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.models.UsuarioType;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest // Indica que é uma classe de testes
@ExtendWith(MockitoExtension.class) // Integração com Mockito e JUnit 5, instancia os mocks
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private TransactionService transactionService;


    // Configuração de Usuários
    private Usuario createUsuario(Long id, String nome, String sobrenome, BigDecimal balance) {
        return new Usuario(id, nome, sobrenome, "9999999990" + id, nome.toLowerCase() + "@gmail.com", "123456", balance, UsuarioType.COMMON);
    }

    // Configuração de Mocks Padrão
    private void setupMockForUsuarios(Usuario sender,
            Usuario receiver) throws Exception {
        when(usuarioService.findUsuarioById(sender.getId())).thenReturn(sender);
        when(usuarioService.findUsuarioById(receiver.getId())).thenReturn(receiver);
    }


    @Test
    @DisplayName("Must throw UnauthorizedTransactionException when transaction is unauthorized.")
    public void createTransactionFailedUnauthorizedTransaction() throws Exception {
        // Criando usuários de teste
        Usuario sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        Usuario receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));

        // Definindo o que ocorrerá na chamada de cada método mockado na hora de testar o método principal
        setupMockForUsuarios(sender, receiver);
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(false);

        // Verificar se a exceção foi lançada
        Assertions.assertThrows(UnauthorizedTransactionException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);
            this.transactionService.createTransaction(transactionDTO);
        });
    }

    @Test
    @DisplayName("Must rollback transaction and throw NotificationServiceOfflineException when notification service is offline.")
    public void createTransactionFailedNotificationServiceIsOffline() throws Exception {
        // Criando usuários de teste
        Usuario sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        Usuario receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));

        // Definindo o que ocorrerá na chamada de cada método mockado na hora de testar o método principal
        setupMockForUsuarios(sender, receiver);
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(true);
        when(this.notificationService.notifyUserIsOnline()).thenReturn(false);

        // Verificar se a exceção foi lançada
        Assertions.assertThrows(NotificationServiceOfflineException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);
            this.transactionService.createTransaction(transactionDTO);
        });

        // Verifica se o saldo foi modificado e devolvido após o estorno da transação
        Assertions.assertEquals(new BigDecimal("10"), sender.getBalance());
        Assertions.assertEquals(new BigDecimal("20"), receiver.getBalance());
        verify(this.usuarioService, times(2)).saveUsuario(sender);
        verify(this.usuarioService, times(2)).saveUsuario(receiver);
    }

    @Test
    @DisplayName("Must create transaction in DB when everything is ok.")
    public void createTransactionSuccess() throws Exception {
        // Criando usuários de teste
        Usuario sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        Usuario receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));

        // Definindo o que ocorrerá na chamada de cada método mockado na hora de testar o método principal
        setupMockForUsuarios(sender, receiver);
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(true);
        when(this.notificationService.notifyUserIsOnline()).thenReturn(true);

        // Criando transação
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);

        // Iniciando o método createTransaction
        transactionService.createTransaction(transactionDTO);

        // Verificando se o saldo foi modificado e se os usuários foram salvos
        Assertions.assertEquals(new BigDecimal("0"), sender.getBalance());
        Assertions.assertEquals(new BigDecimal("30"), receiver.getBalance());
        verify(this.usuarioService, times(1)).saveUsuario(sender);
        verify(this.usuarioService, times(1)).saveUsuario(receiver);

        // Verificar se a notificação foi enviada
        verify(this.notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso.");
        verify(this.notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso.");

        // Verificar se a transação foi salva
        verify(this.transactionRepository).save(argThat(transaction ->
                transaction.getAmount().equals(new BigDecimal("10")) && transaction.getSender().equals(sender) &&
                        transaction.getReceiver().equals(receiver)));
    }
}