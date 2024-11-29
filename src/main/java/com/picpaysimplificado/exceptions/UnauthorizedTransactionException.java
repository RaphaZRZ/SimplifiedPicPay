package com.picpaysimplificado.exceptions;

public class UnauthorizedTransactionException extends Exception {
    public UnauthorizedTransactionException() {
        super("Transação não autorizada.");
    }
}
