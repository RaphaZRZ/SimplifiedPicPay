package com.picpaysimplificado.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull(message = "{amount.notNull}") BigDecimal amount,
        @NotNull(message = "{sender.notNull}") Long senderId,
        @NotNull(message = "{receiver.notNull}") Long receiverId) {
}
