package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class InvalidCNPJException extends Exception {
    private final int statusCode;

    public InvalidCNPJException() {
        super("CNPJ inválido.");
        this.statusCode = 400;
    }
}
