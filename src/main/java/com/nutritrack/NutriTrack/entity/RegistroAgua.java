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

/**
 * Entidade que representa um registro de consumo diário de água de um usuário.
 * Contém informações sobre a quantidade de água consumida e a data da medição.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do registro de água (chave primária)</li>
 *   <li>usuario: Referência ao usuário dono do registro (ManyToOne, obrigatório)</li>
 *   <li>quantidadeMl: Quantidade de água consumida em mililitros, obrigatória</li>
 *   <li>dataMedicao: Data em que a medição foi realizada, obrigatória</li>
 *   <li>observadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 * </ul>
 */
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
