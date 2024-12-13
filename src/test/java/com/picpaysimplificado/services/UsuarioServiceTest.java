package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.exceptions.*;
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
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    @DisplayName("Must throw UserNotFoundException if user not found by id.")
    public void findUsuarioByIdFailedUserNotFound() {
        // Jogando a exceçeção caso o usuário não seja encontrado
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.usuarioService.findUsuarioById(1L);
        });
    }

    @Test
    @DisplayName("Must find the user by id when everything is ok.")
    public void findUsuarioByIdSuccess() throws Exception {
        Usuario user = createUsuario(new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno do método findById
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        Usuario mockedUser = this.usuarioService.findUsuarioById(1L);

        // Verificando se o usuário foi retornado corretamente
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Must throw UserNotFoundException if user not found by document.")
    public void findUsuarioByDocumentFailedUserNotFound() {
        // Jogando a exceçeção caso o usuário não seja encontrado
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.usuarioService.findUsuarioByDocument("99999999902");
        });
    }

    @Test
    @DisplayName("Must find the user by document when everything is ok.")
    public void findUsuarioByDocumentSuccess() throws Exception {
        Usuario user = createUsuario(new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno do método findUsuarioByDocument
        when(this.usuarioRepository.findUsuarioByDocument("99999999901")).thenReturn(Optional.of(user));

        Usuario mockedUser = this.usuarioService.findUsuarioByDocument("99999999901");

        // Verificando se o usuário foi retornado corretamente
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.usuarioRepository, times(1)).findUsuarioByDocument("99999999901");
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
    @DisplayName("Must throw UserNotFoundException exception when attempting to update a user that doesn't exist.")
    void updateUsuarioFailedUserNotFound() {
        // Jogando a exceção caso o usuário não exista
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.usuarioService.updateUsuario(new UpdatePasswordDTO("654321"), 99L);
        });
    }

    @Test
    @DisplayName("Must update the user's password when everything is correct.")
    void updateUsuarioSuccess() throws Exception {
        Usuario user = createUsuario(new BigDecimal("10"), UsuarioType.COMMON);

        // Preparando retorno dos método de validação
        when(this.usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        this.usuarioService.updateUsuario(new UpdatePasswordDTO("654321"), 1L);

        // Verificando se a senha foi alterada
        verify(this.usuarioRepository, times(1)).save(argThat(Usuario ->
                Usuario.getPassword().equals("654321")));
    }

    @Test
    @DisplayName("Must throw UserNotFoundException exception when attempting to delete a user that doesn't exist.")
    void deleteUsuarioFailedUserNotFound() {
        // Jogando a exceção caso o usuário não exista
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.usuarioService.deleteUsuario(1L);
        });
    }

    @Test
    @DisplayName("Must delete the user when everything is correct.")
    void deleteUsuarioSuccess() throws Exception {
        // Preparando retorno dos método de validação
        when(this.usuarioRepository.existsById(1L)).thenReturn(true);

        this.usuarioService.deleteUsuario(1L);

        // Verificando se o usuário foi alterada
        verify(this.usuarioRepository, times(1)).deleteById(1L);
    }
}