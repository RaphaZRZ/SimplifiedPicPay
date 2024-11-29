package com.picpaysimplificado.exceptions;

public class InvalidCNPJException extends Exception {
    public InvalidCNPJException() {
        super("CNPJ inválido.");
    }
}
