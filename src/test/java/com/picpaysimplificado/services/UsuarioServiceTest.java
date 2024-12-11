package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.exceptions.DocumentAlreadyExistsException;
import com.picpaysimplificado.exceptions.EmailAlreadyExistsException;
import com.picpaysimplificado.exceptions.InsufficientBalanceException;
import com.picpaysimplificado.exceptions.MerchantTransactionNotAllowedException;
import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.models.UsuarioType;
import com.picpaysimplificado.repositories.UsuarioRepository;
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
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;


    // Configuração de Usuários
    private Usuario createUsuario(BigDecimal balance, UsuarioType type) {
        return new Usuario(1L, "Ana", "Clara", "99999999901", "ana@gmail.com", "123456", balance, type);
    }

    @Test
    @DisplayName("Must throw MerchantTransactionNotAllowedException when a merchant tries to perform a transaction.")
    void validateTransactionFailedMerchantTransactionNotAllowed() {
        Usuario sender = createUsuario(new BigDecimal("10"), UsuarioType.MERCHANT);

        // Jogando a exceção caso o usuário seja um lojista tentando realizar uma transação
        Assertions.assertThrows(MerchantTransactionNotAllowedException.class, () -> {
            this.usuarioService.validateTransaction(sender, new BigDecimal("10"));
        });
    }

    @Test
    @DisplayName("Must throw InsufficientBalanceException when a COMMON user tries to perform a transaction with insufficient funds.")
    void validateTransactionFailedInsufficientBalance() {
        Usuario sender = createUsuario(new BigDecimal("10"), UsuarioType.COMMON);

        // Jogando a exceçeção caso o saldo do usuário seja insuficiente
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            this.usuarioService.validateTransaction(sender, new BigDecimal("50"));
        });
    }

    @Test
    @DisplayName("Must throw EmailAlreadyExistsException when attempting to register a user with an already registered email.")
    void createUsuarioFailedEmailAlreadyExists() {
        UsuarioDTO userData = new UsuarioDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno do método existByEmail
        when(this.usuarioRepository.existsByEmail(userData.email())).thenReturn(true);

        // Jogando a exceção caso o email já esteja cadastrado no banco de dados
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            this.usuarioService.createUsuario(userData);
        });
    }

    @Test
    @DisplayName("Must throw DocumentAlreadyExistsException when attempting to register a user with an already registered document.")
    void createUsuarioFailedDocumentAlreadyExists() {
        UsuarioDTO userData = new UsuarioDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno do método existByEmail
        when(this.usuarioRepository.existsByDocument(userData.document())).thenReturn(true);

        // Jogando a exceção caso o email já esteja cadastrado no banco de dados
        Assertions.assertThrows(DocumentAlreadyExistsException.class, () -> {
            this.usuarioService.createUsuario(userData);
        });
    }

    @Test
    @DisplayName("Must create User in DB when everything is ok.")
    void createUsuarioSuccess() throws Exception {
        UsuarioDTO userData = new UsuarioDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno dos método de validação
        when(this.usuarioRepository.existsByEmail(userData.email())).thenReturn(false);
        when(this.usuarioRepository.existsByDocument(userData.document())).thenReturn(false);

        this.usuarioService.createUsuario(userData);

        // Verificando se o usuário foi salvo no banco de dados
        verify(this.usuarioRepository, times(1)).save(any());
    }

    @Test
    void updateUsuario() {
    }

    @Test
    void deleteUsuario() {
    }
}