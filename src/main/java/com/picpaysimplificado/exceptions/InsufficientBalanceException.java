package com.picpaysimplificado.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Saldo insuficiente.");
    }
}
