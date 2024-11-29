package com.picpaysimplificado.exceptions;

public class InvalidCPFException extends Exception {
    public InvalidCPFException() {
        super("CPF inválido.");
    }
}
