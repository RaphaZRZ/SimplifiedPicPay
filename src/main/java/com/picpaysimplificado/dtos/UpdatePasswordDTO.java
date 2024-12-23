package com.picpaysimplificado.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotNull(message = "The password cannot be null.") @Size(min = 6, max = 60, message = "The password must be between 6 and 60 characters long.") String password) {
}
