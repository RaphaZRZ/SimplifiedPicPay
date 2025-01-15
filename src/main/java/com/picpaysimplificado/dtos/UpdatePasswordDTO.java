package com.picpaysimplificado.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotNull(message = "{password.notNull}") @Size(min = 6, max = 60, message = "{password.size}") String password) {
}
