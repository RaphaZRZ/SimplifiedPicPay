package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends Exception {
    private final int statusCode;

    public InsufficientBalanceException() {
        super("Saldo insuficiente.");
        this.statusCode = 400;
    }
}
