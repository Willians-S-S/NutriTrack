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

/**
 * Entidade que representa uma meta nutricional de um usuário.
 * Contém informações sobre o tipo de meta, os objetivos nutricionais e o período de validade.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único da meta (chave primária)</li>
 *   <li>usuario: Referência ao usuário dono da meta (ManyToOne, obrigatório)</li>
 *   <li>tipo: Tipo da meta (diária, semanal, mensal), obrigatório</li>
 *   <li>caloriasObjetivo: Objetivo de calorias a serem consumidas, obrigatório</li>
 *   <li>proteinasObjetivo: Objetivo de proteínas em gramas a serem consumidas, obrigatório</li>
 *   <li>carboidratosObjetivo: Objetivo de carboidratos em gramas a serem consumidos, obrigatório</li>
 *   <li>gordurasObjetivo: Objetivo de gorduras em gramas a serem consumidas, obrigatório</li>
 *   <li>criadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 *   <li>atualizadoEm: Data e hora da última atualização do registro, preenchida automaticamente</li>
 * </ul>
 */
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

//    @Column(nullable = false)
//    private LocalDate dataInicio;

//    @Column(nullable = false)
//    private LocalDate dataFim;

    @CreationTimestamp
    private OffsetDateTime criadoEm;

    @UpdateTimestamp
    private OffsetDateTime atualizadoEm;
}
