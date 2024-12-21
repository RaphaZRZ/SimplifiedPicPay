package com.picpaysimplificado.configs;

import com.picpaysimplificado.dtos.ExceptionDTO;
import com.picpaysimplificado.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice // Inicializa a classe na inicialização do Spring Boot
public class ControllerExceptionHandler {
    // CPF inválido
    @ExceptionHandler(InvalidCPFException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidCPFException(InvalidCPFException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // CNPJ inválido
    @ExceptionHandler(InvalidCNPJException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidCNPJException(InvalidCNPJException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Documento já cadastrado
    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleDocumentAlreadyExistsException(DocumentAlreadyExistsException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Email já cadastrado
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Usuário já cadastrado
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateEntry(DataIntegrityViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Usuário já cadastrado.", 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Saldo insuficiente
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionDTO> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Dados inválidos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleDeserialization(HttpMessageNotReadableException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Erro de deserialização. Os dados enviados são inválidos.", 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Validar anotações BEAN na hora de salvar ou alterar dados no banco de dados
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleValidationException(ConstraintViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Erro na validação: " + exception.getMessage(), 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Validar anotações BEAN quando um objeto é inválido como argumento de um end-point
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleBeanValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream().findFirst().map(ObjectError::getDefaultMessage).orElse("Erro de validação.");
        ExceptionDTO exceptionDTO = new ExceptionDTO(errorMessage, 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Tipo do parâmetro passados na URL da requisição inválido
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = String.format("Entrada inválida: O parâmetro '%s' fornecido é de um tipo inválido.", exception.getName());
        ExceptionDTO exceptionDTO = new ExceptionDTO(message, 400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Transação de lojista não autorizada
    @ExceptionHandler(MerchantTransactionNotAllowedException.class)
    public ResponseEntity<ExceptionDTO> handleMerchantTransactionNotAllowedException(MerchantTransactionNotAllowedException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(403).body(exceptionDTO);
    }

    // Transação não autorizada
    @ExceptionHandler(UnauthorizedTransactionException.class)
    public ResponseEntity<ExceptionDTO> handleUnauthorizedTransactionException(UnauthorizedTransactionException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(403).body(exceptionDTO);
    }

    // Usuário já existe
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUserNotFoundException(UserNotFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.status(404).body(exceptionDTO);
    }

    // Controlador para classe não encontrado
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionDTO> handleControllerNotFoundException(NoHandlerFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Controlador inexistente para esta URL.", 404);
        return ResponseEntity.status(404).body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handle404(EntityNotFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Recurso não encontrado.", 404);
        return ResponseEntity.status(404).body(exceptionDTO);
    }

    // Serviço de notificações fora do ar
    @ExceptionHandler(NotificationServiceOfflineException.class)
    public ResponseEntity<ExceptionDTO> handleNotificationServiceOfflineException(NotificationServiceOfflineException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), exception.getStatusCode());
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), 500);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
