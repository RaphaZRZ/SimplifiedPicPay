package com.picpaysimplificado.configs;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.picpaysimplificado.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Inicializa a classe na inicialização do Spring Boot
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class) // Validar se o usuário já está cadastrado
    public ResponseEntity handleDuplicateEntry(DataIntegrityViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Usuário já cadastrado.", "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Verificar dados inválidos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleDeserialization(HttpMessageNotReadableException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Erro de deserialização. Dados inválidos enviados.", "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Verificar anotações BEAN na hora de salvar ou alterar dados no banco de dados
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleValidationException(ConstraintViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Erro na validação: " + exception.getMessage(), "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    // Verificar anotações BEAN quando um objeto é inválido como argumento de um end-point
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBeanValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream().findFirst().map(ObjectError::getDefaultMessage).orElse("Erro de validação.");
        ExceptionDTO exceptionDTO = new ExceptionDTO(errorMessage, "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handle404(EntityNotFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("Recurso não encontrado.", "404");
        return ResponseEntity.status(404).body(exceptionDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
