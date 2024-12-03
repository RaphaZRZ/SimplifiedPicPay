package com.picpaysimplificado.exceptions;

public class DocumentAlreadyExistsException extends Exception {
    public DocumentAlreadyExistsException() {
        super("Documento já cadastrado.");
    }
}
