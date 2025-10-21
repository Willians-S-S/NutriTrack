package com.nutritrack.NutriTrack.entity;

import com.nutritrack.NutriTrack.enums.TipoMeta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "metas")
@Getter
@Setter
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMeta tipo;

    @Column(nullable = false)
    private BigDecimal caloriasObjetivo;

    @Column(nullable = false)
    private BigDecimal proteinasObjetivo;

    @Column(nullable = false)
    private BigDecimal carboidratosObjetivo;

    @Column(nullable = false)
    private BigDecimal gordurasObjetivo;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @CreationTimestamp
    private OffsetDateTime criadoEm;

    @UpdateTimestamp
    private OffsetDateTime atualizadoEm;
}
