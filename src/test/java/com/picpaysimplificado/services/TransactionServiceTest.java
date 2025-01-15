package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.exceptions.NotificationServiceOfflineException;
import com.picpaysimplificado.exceptions.UnauthorizedTransactionException;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.models.UserType;
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
@DataJpaTest
@ExtendWith(MockitoExtension.class) // Integration with Mockito and JUnit 5, initializes the mocks
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private TransactionService transactionService;


    // create user shortcut
    private User createUsuario(Long id, String nome, String sobrenome, BigDecimal balance) {
        return new User(id, nome, sobrenome, "9999999990" + id, nome.toLowerCase() + "@gmail.com", "123456", balance, UserType.COMMON);
    }

    private void setupMockForUsers(User sender, User receiver) throws Exception {
        when(userService.findUserById(sender.getId())).thenReturn(sender);
        when(userService.findUserById(receiver.getId())).thenReturn(receiver);
    }


    @Test
    @DisplayName("Must throw UnauthorizedTransactionException when transaction is unauthorized.")
    public void createTransactionFailedUnauthorizedTransaction() throws Exception {
        // Creating test users
        User sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        User receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));
        setupMockForUsers(sender, receiver);

        // Preparing the return value of transactionIsAuthorized method
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(false);

        // Throwing an exception if the transaction is unauthorized
        Assertions.assertThrows(UnauthorizedTransactionException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);
            this.transactionService.createTransaction(transactionDTO);
        });
    }

    @Test
    @DisplayName("Must rollback transaction and throw NotificationServiceOfflineException when notification service is offline.")
    public void createTransactionFailedNotificationServiceIsOffline() throws Exception {
        // Creating test users
        User sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        User receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));
        setupMockForUsers(sender, receiver);

        // Preparing the return values of transactionIsAuthorized and notifyUserIsOnline methods
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(true);
        when(this.notificationService.notifyUserIsOnline()).thenReturn(false);

        // Throwing an exception if notification service is offline
        Assertions.assertThrows(NotificationServiceOfflineException.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);
            this.transactionService.createTransaction(transactionDTO);
        });

        // Verifying if the sender's money has been returned successfully
        Assertions.assertEquals(new BigDecimal("10"), sender.getBalance());
        Assertions.assertEquals(new BigDecimal("20"), receiver.getBalance());
        verify(this.userService, times(2)).saveUser(sender);
        verify(this.userService, times(2)).saveUser(receiver);
    }

    @Test
    @DisplayName("Must create transaction in DB when everything is ok.")
    public void createTransactionSuccess() throws Exception {
        // Creating test users
        User sender = createUsuario(1L, "Ana", "Clara", new BigDecimal("10"));
        User receiver = createUsuario(2L, "Bernardo", "Artheus", new BigDecimal("20"));
        setupMockForUsers(sender, receiver);

        // Preparing the return values of transactionIsAuthorized and notifyUserIsOnline methods
        when(this.authorizationService.transactionIsAuthorized()).thenReturn(true);
        when(this.notificationService.notifyUserIsOnline()).thenReturn(true);

        // creating transacation
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("10"), 1L, 2L);

        // Initializing the createTransaction method
        transactionService.createTransaction(transactionDTO);

        // Verifying if the users' balances have been updated and saved successfully
        Assertions.assertEquals(new BigDecimal("0"), sender.getBalance());
        Assertions.assertEquals(new BigDecimal("30"), receiver.getBalance());
        verify(this.userService, times(1)).saveUser(sender);
        verify(this.userService, times(1)).saveUser(receiver);

        // Verifying if the notification has been sent
        verify(this.notificationService, times(1)).sendNotification(sender, "Transaction completed successfully.");
        verify(this.notificationService, times(1)).sendNotification(receiver, "Transaction received successfully.");

        // Verifying if the transaction has been saved
        verify(this.transactionRepository).save(argThat(transaction ->
                transaction.getAmount().equals(new BigDecimal("10")) && transaction.getSender().equals(sender) &&
                        transaction.getReceiver().equals(receiver)));
    }
}