package com.picpaysimplificado.exceptions;

public class MerchantTransactionNotAllowedException extends Exception {
    public MerchantTransactionNotAllowedException() {
        super("Usuário do tipo lojista não está autorizado a realizar transação.");
    }
}