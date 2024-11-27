package com.picpaysimplificado.model;

import com.picpaysimplificado.dtos.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20, message = "O nome deve conter de 3 a 20 caracteres.")
    @Column(name = "nome")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 20, message = "O sobrenome deve conter de 3 a 20 caracteres.")
    @Column(name = "sobrenome")
    private String lastName;

    @NotNull
    @Size(min = 11, max = 14, message = "Documento inválido.")
    @Column(name = "documento")
    private String document;

    @NotNull
    @Email(message = "email inválido.")
    @Column(name = "Email", unique = true)
    private String email;

    @NotNull
    @Column(name = "senha")
    private String password;

    @Column(name = "saldo")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private UsuarioType usuarioType;

    public Usuario(UsuarioDTO data) {
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.document = data.document();
        this.email = data.email();
        this.password = data.password();
        this.balance = data.balance();
        this.usuarioType = data.usuarioType();
    }
}
