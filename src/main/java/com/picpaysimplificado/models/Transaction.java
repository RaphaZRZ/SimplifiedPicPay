package com.picpaysimplificado.models;

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

    @NotNull(message = "{amount.notNull}")
    @Column(name = "valor")
    private BigDecimal amount;

    @ManyToOne
    @NotNull(message = "{sender.notNull}")
    @JoinColumn(name = "pagador_id")
    private User sender;

    @ManyToOne
    @NotNull(message = "{receiver.notNull}")
    @JoinColumn(name = "recebedor_id")
    private User receiver;

    @NotNull(message = "{timestamp.notNull}")
    @Column(name = "horario_transacao")
    private LocalDateTime timestamp;
}
