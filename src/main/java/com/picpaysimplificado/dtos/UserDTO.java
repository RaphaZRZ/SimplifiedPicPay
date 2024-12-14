package com.picpaysimplificado.dtos;

import com.picpaysimplificado.models.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserDTO(
        @NotNull @Size(min = 3, max = 20, message = "O nome deve conter de 3 a 20 caracteres.") String firstName,
        @NotNull @Size(min = 3, max = 20, message = "O sobrenome deve conter de 3 a 20 caracteres.") String lastName,
        @NotNull String document,
        @NotNull @Email(message = "email inválido.") String email,
        @NotNull @Size(min = 6, max = 60, message = "A senha deve conter de 6 a 60 caracteres.") String password,
        @NotNull BigDecimal balance,
        @NotNull UserType userType) {
}