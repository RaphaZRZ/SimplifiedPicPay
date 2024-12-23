package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class DocumentAlreadyExistsException extends Exception {
    private final int statusCode;

    public DocumentAlreadyExistsException() {
        super("Document already registered.");
        this.statusCode = 400;
    }
}
