package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final int statusCode;

    public UserNotFoundException() {
        super("Usuário não encontrado.");
        this.statusCode = 404;
    }
}
