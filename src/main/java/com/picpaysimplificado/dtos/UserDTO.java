package com.picpaysimplificado.dtos;

import com.picpaysimplificado.models.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserDTO(
        @NotNull @Size(min = 3, max = 20, message = "The first name must be between 3 and 20 characters long.") String firstName,
        @NotNull @Size(min = 3, max = 20, message = "The last name must be between 3 and 20 characters long.") String lastName,
        @NotNull String document,
        @NotNull @Email(message = "Invalid email.") String email,
        @NotNull @Size(min = 6, max = 60, message = "The password must be between 6 and 60 characters long.") String password,
        @NotNull BigDecimal balance,
        @NotNull UserType userType) {
}