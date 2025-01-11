package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.exceptions.*;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.models.UserType;
import com.picpaysimplificado.repositories.UserRepository;
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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    // Configuração de Usuários
    private User createUser(BigDecimal balance, UserType type) {
        return new User(1L, "Ana", "Clara", "99999999901", "ana@gmail.com", "123456", balance, type);
    }

    @Test
    @DisplayName("Must throw UserNotFoundException if user not found by id.")
    public void findUserByIdFailedUserNotFound() {
        // Jogando a exceçeção caso o usuário não seja encontrado
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findUserById(1L);
        });
    }

    @Test
    @DisplayName("Must find the user by id when everything is ok.")
    public void findUserByIdSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno do método findById
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        User mockedUser = this.userService.findUserById(1L);

        // Verificando se o usuário foi retornado corretamente
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Must throw UserNotFoundException if user not found by document.")
    public void findUserByDocumentFailedUserNotFound() {
        // Jogando a exceçeção caso o usuário não seja encontrado
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findUserByDocument("99999999902");
        });
    }

    @Test
    @DisplayName("Must find the user by document when everything is ok.")
    public void findUserByDocumentSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno do método findUsuarioByDocument
        when(this.userRepository.findByDocument("99999999901")).thenReturn(Optional.of(user));

        User mockedUser = this.userService.findUserByDocument("99999999901");

        // Verificando se o usuário foi retornado corretamente
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.userRepository, times(1)).findByDocument("99999999901");
    }

    @Test
    @DisplayName("Must throw MerchantTransactionNotAllowedException when a merchant tries to perform a transaction.")
    void validateTransactionFailedMerchantTransactionNotAllowed() {
        User sender = createUser(new BigDecimal("10"), UserType.MERCHANT);

        // Jogando a exceção caso o usuário seja um lojista tentando realizar uma transação
        Assertions.assertThrows(MerchantTransactionNotAllowedException.class, () -> {
            this.userService.validateTransaction(sender, new BigDecimal("10"));
        });
    }

    @Test
    @DisplayName("Must throw InsufficientBalanceException when a COMMON user tries to perform a transaction with insufficient funds.")
    void validateTransactionFailedInsufficientBalance() {
        User sender = createUser(new BigDecimal("10"), UserType.COMMON);

        // Jogando a exceçeção caso o saldo do usuário seja insuficiente
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            this.userService.validateTransaction(sender, new BigDecimal("50"));
        });
    }

    @Test
    @DisplayName("Must throw EmailAlreadyExistsException when attempting to register a user with an already registered email.")
    void createUserFailedEmailAlreadyExists() {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno do método existByEmail
        when(this.userRepository.existsByEmail(userData.email())).thenReturn(true);

        // Jogando a exceção caso o email já esteja cadastrado no banco de dados
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            this.userService.createUser(userData);
        });
    }

    @Test
    @DisplayName("Must throw DocumentAlreadyExistsException when attempting to register a user with an already registered document.")
    void createUserFailedDocumentAlreadyExists() {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno do método existByEmail
        when(this.userRepository.existsByDocument(userData.document())).thenReturn(true);

        // Jogando a exceção caso o email já esteja cadastrado no banco de dados
        Assertions.assertThrows(DocumentAlreadyExistsException.class, () -> {
            this.userService.createUser(userData);
        });
    }

    @Test
    @DisplayName("Must create User in DB when everything is ok.")
    void createUserSuccess() throws Exception {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno dos método de validação
        when(this.userRepository.existsByEmail(userData.email())).thenReturn(false);
        when(this.userRepository.existsByDocument(userData.document())).thenReturn(false);

        this.userService.createUser(userData);

        // Verificando se o usuário foi salvo no banco de dados
        verify(this.userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Must throw UserNotFoundException exception when attempting to update a user that doesn't exist.")
    void updateUserByIdFailedNotFound() {
        // Jogando a exceção caso o usuário não exista
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.updateUserById(new UpdatePasswordDTO("654321"), 99L);
        });
    }

    @Test
    @DisplayName("Must update the user's password when everything is correct.")
    void updateUserByIdSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparando retorno dos método de validação
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        this.userService.updateUserById(new UpdatePasswordDTO("654321"), 1L);

        // Verificando se a senha foi alterada
        verify(this.userRepository, times(1)).save(argThat(Usuario ->
                Usuario.getPassword().equals("654321")));
    }

    @Test
    @DisplayName("Must throw UserNotFoundException exception when attempting to delete a user that doesn't exist.")
    void deleteUserByIdFailedIdNotFound() {
        // Jogando a exceção caso o usuário não exista
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.deleteUserById(1L);
        });
    }

    @Test
    @DisplayName("Must delete the user when everything is correct.")
    void deleteUserByIdSuccess() throws Exception {
        // Preparando retorno dos método de validação
        when(this.userRepository.existsById(1L)).thenReturn(true);

        this.userService.deleteUserById(1L);

        // Verificando se o usuário foi alterada
        verify(this.userRepository, times(1)).deleteById(1L);
    }
}