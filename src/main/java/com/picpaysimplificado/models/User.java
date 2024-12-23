package com.picpaysimplificado.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.serializers.DocumentSerializer;
import com.picpaysimplificado.serializers.PasswordSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "usuarios")
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20, message = "The first name must be between 3 and 20 characters long.")
    @Column(name = "nome")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 20, message = "The last name must be between 3 and 20 characters long.")
    @Column(name = "sobrenome")
    private String lastName;

    @NotNull
    @JsonSerialize(using = DocumentSerializer.class)
    @Column(name = "documento", unique = true)
    private String document;

    @NotNull
    @Email(message = "Invalid email.")
    @Column(name = "Email", unique = true)
    private String email;

    @NotNull
    @Size(min = 6, max = 60, message = "The password must be between 6 and 60 characters long.")
    @JsonSerialize(using = PasswordSerializer.class)
    @Column(name = "senha")
    private String password;

    @NotNull
    @Column(name = "saldo")
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private UserType userType;

    public User(UserDTO userData) throws Exception {
        this.firstName = userData.firstName();
        this.lastName = userData.lastName();
        this.document = userData.document();
        this.email = userData.email();
        this.password = userData.password();
        this.balance = userData.balance();
        this.userType = userData.userType();
    }
}
