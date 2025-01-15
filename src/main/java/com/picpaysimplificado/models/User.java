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

    @NotNull(message = "{firstName.notNull}")
    @Size(min = 3, max = 20, message = "The first name must be between 3 and 20 characters long.")
    @Column(name = "nome")
    private String firstName;

    @NotNull(message = "{lastName.notNull}")
    @Size(min = 3, max = 20, message = "The last name must be between 3 and 20 characters long.")
    @Column(name = "sobrenome")
    private String lastName;

    @NotNull(message = "{document.notNull}")
    @JsonSerialize(using = DocumentSerializer.class)
    @Column(name = "documento", unique = true)
    private String document;

    @NotNull(message = "{email.notNull}")
    @Email(message = "{email.invalid}")
    @Column(name = "Email", unique = true)
    private String email;

    @NotNull(message = "{password.notNull}")
    @Size(min = 6, max = 60, message = "{password.size}")
    @JsonSerialize(using = PasswordSerializer.class)
    @Column(name = "senha")
    private String password;

    @NotNull(message = "{balance.notNull}")
    @Column(name = "saldo")
    private BigDecimal balance;

    @NotNull(message = "{userType.notNull}")
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
