package com.picpaysimplificado.configs;

import com.picpaysimplificado.dtos.ExceptionDTO;
import com.picpaysimplificado.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice // Initialize the class during Spring Boot initialization
public class ControllerExceptionHandler {
    // Invalid CPF
    @ExceptionHandler(InvalidCPFException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidCPFException(InvalidCPFException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Invalid CNPJ
    @ExceptionHandler(InvalidCNPJException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidCNPJException(InvalidCNPJException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Insufficient balance
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionDTO> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Invalid data
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleDeserialization() {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Deserialization error. Invalid data.", 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Validate BEAN annotations during save or update of data in the database
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(ConstraintViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Validation error: " + exception.getMessage(), 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Validate BEAN annotations when an object is invalid as an argument of an endpoint
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleBeanValidationException(MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errorMessage.append(String.format("%s; ", message));
        });

        ExceptionDTO exceptionDTO = new ExceptionDTO(errorMessage.toString(), 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Invalid parameter type in the URL of the request
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = String.format("Invalid entry: The parameter '%s' provided is of an invalid type.", exception.getName());
        ExceptionDTO exceptionDTO = new ExceptionDTO(message, 404);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Merchant transaction not allowed
    @ExceptionHandler(MerchantTransactionNotAllowedException.class)
    public ResponseEntity<ExceptionDTO> handleMerchantTransactionNotAllowedException(MerchantTransactionNotAllowedException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Unauthorized transaction
    @ExceptionHandler(UnauthorizedTransactionException.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorizedTransactionException(UnauthorizedTransactionException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // User not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUserNotFoundException(UserNotFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Class controller not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDTO> handleControllerNotFoundException(NoHandlerFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Non-existent controller for this URL", 404);
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handle404() {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Resource not found.", 404);
        return ResponseEntity.status(404).body(exceptionDTO);
    }

    // Document already registered
    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleDocumentAlreadyExistsException(DocumentAlreadyExistsException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Email already registered
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // User already registered
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateEntry() {
        ExceptionDTO exceptionDTO = new ExceptionDTO("User already registered.", 409);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Notification service is offline
    @ExceptionHandler(NotificationServiceOfflineException.class)
    public ResponseEntity<ExceptionDTO> handleNotificationServiceOfflineException(NotificationServiceOfflineException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(exception.getStatusCode()).body(exceptionDTO);
    }

    // Generals exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), 500);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
