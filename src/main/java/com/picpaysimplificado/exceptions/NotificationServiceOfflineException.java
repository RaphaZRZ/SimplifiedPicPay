package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class NotificationServiceOfflineException extends Exception {
    private final int statusCode;

    public NotificationServiceOfflineException() {
        super("Serviço de notificação está fora do ar. A transação será revertida.");
        statusCode = 500;
    }
}
