package com.picpaysimplificado.dtos;

import java.math.BigDecimal;

// Data Transfers Objects
public record TransactionDTO(BigDecimal amount, Long senderId, Long receiverId) {
}
