package com.picpaysimplificado.exceptions;

import lombok.Getter;

@Getter
public class NotificationServiceOfflineException extends Exception {
    private final int statusCode;

    public NotificationServiceOfflineException() {
        super("Notification service is offline. The transaction will be rolled back.");
        statusCode = 500;
    }
}
