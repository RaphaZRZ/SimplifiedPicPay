package com.picpaysimplificado.exceptions;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException() {
        super("Email já cadastrado.");
    }
}
