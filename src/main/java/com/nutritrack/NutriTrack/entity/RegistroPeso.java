package com.nutritrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registros_peso", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_usuario", "data_medicao"})
})
public class RegistroPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_registro")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "peso_kg", nullable = false, precision = 6, scale = 3)
    private BigDecimal pesoKg;

    @Column(name = "data_medicao", nullable = false)
    private LocalDate dataMedicao;

    @CreationTimestamp
    @Column(name = "observado_em", updatable = false)
    private OffsetDateTime observadoEm;
}
