package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends Exception {
    private final int statusCode;

    public EmailAlreadyExistsException() {
        super("Email already registered.");
        this.statusCode = 409;
    }
}
