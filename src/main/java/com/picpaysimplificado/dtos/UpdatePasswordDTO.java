package com.picpaysimplificado.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO(
        @NotNull(message = "A senha não pode ser nula.") @Size(min = 6, max = 60, message = "A senha deve conter de 6 a 60 caracteres.") String password) {
}
