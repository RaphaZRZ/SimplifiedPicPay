package com.picpaysimplificado.services;

import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.models.UsuarioType;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Integração com Mockito e JUnit 5
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

    @Test
    @DisplayName("Must create transaction in DB when everything is ok.")
    void createTransactionSuccess() throws Exception{
        Usuario sender = new Usuario(1L, "Ana", "Clara", "99999999901", "ana@gmail.com", "12345", new BigDecimal("10"), UsuarioType.COMMON);
        Usuario receiver = new Usuario(2L, "Bernardo", "Artheus", "99999999902", "bernardo@gmail.com", "12345", new BigDecimal("20"), UsuarioType.COMMON);

        when(usuarioService.findUsuarioById(1L)).thenReturn(sender);
        when(usuarioService.findUsuarioById(2L)).thenReturn(receiver);

        when(authorizationService.transactionIsAuthorized()).thenReturn(true);


    }
}