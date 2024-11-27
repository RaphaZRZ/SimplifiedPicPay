package com.picpaysimplificado.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Transacao")
@Table(name = "Transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "valor")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "pagador_id")
    private Usuario sender;

    @ManyToOne
    @JoinColumn(name = "recebedor_id")
    private Usuario receiver;

    @Column(name = "horario_transacao")
    private LocalDateTime timestamp;
}
