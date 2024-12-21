package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class DocumentAlreadyExistsException extends Exception {
    private final int statusCode;

    public DocumentAlreadyExistsException() {
        super("Documento já cadastrado.");
        this.statusCode = 400;
    }
}
