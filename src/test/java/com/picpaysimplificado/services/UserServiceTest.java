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


    // create user shortcut
    private User createUser(BigDecimal balance, UserType type) {
        return new User(1L, "Ana", "Clara", "00000000001", "ana@gmail.com", "defaultPassword", balance, type);
    }

    // findUserById
    @Test
    @DisplayName("Must throw UserNotFoundException if user not found by id.")
    public void findUserByIdFailedUserNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findUserById(1L);
        });
    }

    @Test
    @DisplayName("Must find the user by id when everything is ok.")
    public void findUserByIdSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparing the return values of findById method
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        User mockedUser = this.userService.findUserById(1L);

        // Verifying if the user has been returned correctly
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.userRepository, times(1)).findById(1L);
    }

    // findUserByDocument
    @Test
    @DisplayName("Must throw UserNotFoundException if user not found by document.")
    public void findUserByDocumentFailedUserNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.findUserByDocument("99999999902");
        });
    }

    @Test
    @DisplayName("Must find the user by document when everything is ok.")
    public void findUserByDocumentSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparing the return values of findByDocument method
        when(this.userRepository.findByDocument("99999999901")).thenReturn(Optional.of(user));

        User mockedUser = this.userService.findUserByDocument("99999999901");

        // Verifying if the user has been returned correctly
        Assertions.assertEquals(user, mockedUser, "The user must match the mocked user.");
        verify(this.userRepository, times(1)).findByDocument("99999999901");
    }

    // validateTransaction
    @Test
    @DisplayName("Must throw MerchantTransactionNotAllowedException when a merchant tries to perform a transaction.")
    void validateTransactionFailedMerchantTransactionNotAllowed() {
        User sender = createUser(new BigDecimal("10"), UserType.MERCHANT);

        // Throwing an exception if a merchant attempts to perform a transaction
        Assertions.assertThrows(MerchantTransactionNotAllowedException.class, () -> {
            this.userService.validateTransaction(sender, new BigDecimal("10"));
        });
    }

    @Test
    @DisplayName("Must throw InsufficientBalanceException when a COMMON user tries to perform a transaction with insufficient funds.")
    void validateTransactionFailedInsufficientBalance() {
        User sender = createUser(new BigDecimal("10"), UserType.COMMON);

        // Throwing an exception if user's balance is insufficient
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            this.userService.validateTransaction(sender, new BigDecimal("50"));
        });
    }


    // createUser
    @Test
    @DisplayName("Must throw EmailAlreadyExistsException when attempting to register a user with an already registered email.")
    void createUserFailedEmailAlreadyExists() {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparing the return values of existByEmail method
        when(this.userRepository.existsByEmail(userData.email())).thenReturn(true);

        // Throwing an exception if the email is already registered in database
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            this.userService.createUser(userData);
        });
    }

    @Test
    @DisplayName("Must throw DocumentAlreadyExistsException when attempting to register a user with an already registered document.")
    void createUserFailedDocumentAlreadyExists() {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparing the return values of existByDocument method
        when(this.userRepository.existsByDocument(userData.document())).thenReturn(true);

        // Throwing an exception if the document is already registered in database
        Assertions.assertThrows(DocumentAlreadyExistsException.class, () -> {
            this.userService.createUser(userData);
        });
    }

    @Test
    @DisplayName("Must create User in DB when everything is ok.")
    void createUserSuccess() throws Exception {
        UserDTO userData = new UserDTO("Kopan", "Almeida", "99999999901", "kop@gmail.com", "123456", new BigDecimal("10"), UserType.COMMON);

        // Preparing the return values of existByEmail and existsByDocument methods
        when(this.userRepository.existsByEmail(userData.email())).thenReturn(false);
        when(this.userRepository.existsByDocument(userData.document())).thenReturn(false);

        this.userService.createUser(userData);

        // Verifying if the user has been saved correctly
        verify(this.userRepository, times(1)).save(any());
    }

    // updateUserById
    @Test
    @DisplayName("Must throw UserNotFoundException when attempting to update a non-existent user.")
    void updateUserByIdFailedUserNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.updateUserById(new UpdatePasswordDTO("654321"), 99L);
        });
    }

    @Test
    @DisplayName("Must update the user's password when everything is correct.")
    void updateUserByIdSuccess() throws Exception {
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparing the return value of findById method
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        this.userService.updateUserById(new UpdatePasswordDTO("654321"), 1L);

        // Verifying if the password has been updated
        verify(this.userRepository, times(1)).save(argThat(Usuario ->
                Usuario.getPassword().equals("654321")));
    }

    // updateUserByDocument
    @Test
    @DisplayName("Must throw UserNotFoundException when attempting to update a non-existent user.")
    void updateUserByDocumentFailedUserNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.updateUserByDocument(new UpdatePasswordDTO("999999"), "99999999999");
        });
    }

    @Test
    @DisplayName("Must update the user's password when everything is correct.")
    void updateUserBydDocumentSuccess() throws Exception{
        User user = createUser(new BigDecimal("10"), UserType.COMMON);

        // Preparing the return value of findByDocument method
        when(this.userRepository.findByDocument("99999999901")).thenReturn(Optional.of(user));

        this.userService.updateUserByDocument(new UpdatePasswordDTO("654321"), "99999999901");

        // Verifying if the password has been updated
        verify(this.userRepository, times(1)).save(argThat(Usuario ->
                Usuario.getPassword().equals("654321")));
    }

    // deleteUserById
    @Test
    @DisplayName("Must throw UserNotFoundException exception when attempting to delete a user that doesn't exist.")
    void deleteUserByIdFailedIdNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.deleteUserById(1L);
        });
    }

    @Test
    @DisplayName("Must delete the user when everything is correct.")
    void deleteUserByIdSuccess() throws Exception {
        // Preparing the return values of existById method
        when(this.userRepository.existsById(1L)).thenReturn(true);

        this.userService.deleteUserById(1L);

        // Verifying if the user has been deleted
        verify(this.userRepository, times(1)).deleteById(1L);
    }

    // deleteUserByDocument
    @Test
    @DisplayName("Must throw UserNotFoundException exception when attempting to delete a user that doesn't exist.")
    void deleteUserByDocumentFailedDocumentNotFound() {
        // Throwing an exception if the user is not found
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            this.userService.deleteUserByDocument("99999999999");
        });
    }

    @Test
    @DisplayName("Must delete the user when everything is correct.")
    void deleteUserByDocumentSuccess() throws Exception {
        // Preparing the return values of existById method
        when(this.userRepository.existsByDocument("99999999901")).thenReturn(true);

        this.userService.deleteUserByDocument("99999999901");

        // Verifying if the user has been deleted
        verify(this.userRepository, times(1)).deleteByDocument("99999999901");
    }
}