package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class MerchantTransactionNotAllowedException extends Exception {
    private final int statusCode;

    public MerchantTransactionNotAllowedException() {
        super("Usuário do tipo lojista não está autorizado a realizar transação.");
        this.statusCode = 403;
    }
}