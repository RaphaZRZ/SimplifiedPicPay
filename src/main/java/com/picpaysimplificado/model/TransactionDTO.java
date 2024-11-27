package com.picpaysimplificado.model;

import java.math.BigDecimal;

// Data Transfers Objects
public record TransactionDTO(BigDecimal value, Long senderId, Long receiverId) {
}
