package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class InvalidCPFException extends Exception {
    private final int statusCode;

    public InvalidCPFException() {
        super("CPF inválido.");
        this.statusCode = 400;
    }
}
