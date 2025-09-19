package com.nutritrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registros_agua")
public class RegistroAgua {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_registro")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "quantidade_ml", nullable = false)
    private Integer quantidadeMl;

    @Column(name = "data_medicao", nullable = false)
    private LocalDate dataMedicao;

    @CreationTimestamp
    @Column(name = "observado_em", updatable = false)
    private OffsetDateTime observadoEm;
}
