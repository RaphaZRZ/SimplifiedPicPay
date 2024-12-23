package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private final int statusCode;

    public UserNotFoundException() {
        super("User not found.");
        this.statusCode = 404;
    }
}
