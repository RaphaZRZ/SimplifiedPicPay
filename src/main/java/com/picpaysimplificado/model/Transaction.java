package com.picpaysimplificado.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Transacao")
@Table(name = "Transacao")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column(name = "valor")
    private BigDecimal amount;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "pagador_id")
    private Usuario senderId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "recebedor_id")
    private Usuario receiverId;

    @NotNull
    @Column(name = "horario_transacao")
    private LocalDateTime timestamp;
}
