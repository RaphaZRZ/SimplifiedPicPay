package com.picpaysimplificado.dtos;

import com.picpaysimplificado.model.UsuarioType;

import java.math.BigDecimal;

public record UsuarioDTO(String firstName, String lastName, String document, String email, String password,
                         BigDecimal balance, UsuarioType usuarioType) {
}