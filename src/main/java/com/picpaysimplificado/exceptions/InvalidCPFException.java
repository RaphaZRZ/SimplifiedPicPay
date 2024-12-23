package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class InvalidCPFException extends Exception {
    private final int statusCode;

    public InvalidCPFException() {
        super("Invalid CPF.");
        this.statusCode = 400;
    }
}
