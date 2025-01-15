package com.picpaysimplificado.dtos;

import com.picpaysimplificado.models.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserDTO(
        @NotNull(message = "{firstName.notNull}") @Size(min = 3, max = 20, message = "{firstName.size}") String firstName,
        @NotNull(message = "{lastName.notNull}") @Size(min = 3, max = 20, message = "{lastName.size}") String lastName,
        @NotNull(message = "{document.notNull}") String document,
        @NotNull(message = "{email.notNull}") @Email(message = "{email.invalid}") String email,
        @NotNull(message = "{password.notNull}") @Size(min = 6, max = 60, message = "{password.size}") String password,
        @NotNull(message = "{balance.notNull}") BigDecimal balance,
        @NotNull(message = "{userType.notNull}") UserType userType) {
}