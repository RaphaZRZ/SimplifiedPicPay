package com.picpaysimplificado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "usuarios")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "Nome")
    private String fistName;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "Sobrenome")
    private String lastName;

    @NotNull
    @Size(min = 11, max = 11)
    @Column(name = "Documento")
    private String document;

    @NotNull
    @Column(name = "Email", unique = true)
    private String email;

    @NotNull
    @Column(name = "Senha")
    private String password;

    @Column(name = "Saldo")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo")
    private UsuarioTipo usuarioTipo;
}
