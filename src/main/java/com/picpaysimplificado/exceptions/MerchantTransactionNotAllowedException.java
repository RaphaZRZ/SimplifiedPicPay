package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class MerchantTransactionNotAllowedException extends Exception {
    private final int statusCode;

    public MerchantTransactionNotAllowedException() {
        super("Merchant users are not allowed to perform transactions.");
        this.statusCode = 403;
    }
}