package com.nutritrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alimentos")
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_alimento")
    private UUID id;

    @Column(nullable = false, unique = true, length = 160)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal calorias;

    @Column(name = "proteinas_g", nullable = false, precision = 10, scale = 3)
    private BigDecimal proteinasG;

    @Column(name = "carboidratos_g", nullable = false, precision = 10, scale = 3)
    private BigDecimal carboidratosG;

    @Column(name = "gorduras_g", nullable = false, precision = 10, scale = 3)
    private BigDecimal gordurasG;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private OffsetDateTime criadoEm;
}
