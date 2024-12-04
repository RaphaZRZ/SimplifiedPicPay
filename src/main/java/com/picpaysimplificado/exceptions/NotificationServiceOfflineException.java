package com.picpaysimplificado.exceptions;

public class NotificationServiceOfflineException extends Exception{
    public NotificationServiceOfflineException() {
        super("Serviço de notificação está fora do ar. A transação será revertida.");
    }
}
