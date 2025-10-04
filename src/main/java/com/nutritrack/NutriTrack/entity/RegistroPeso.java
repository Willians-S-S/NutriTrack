package com.nutritrack.NutriTrack.entity;

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

/**
 * Entidade que representa um registro de peso diário de um usuário.
 * Garante que cada usuário tenha no máximo um registro por data.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do registro de peso (chave primária)</li>
 *   <li>usuario: Referência ao usuário dono do registro (ManyToOne, obrigatório)</li>
 *   <li>pesoKg: Peso em quilogramas, obrigatório</li>
 *   <li>dataMedicao: Data da medição do peso, obrigatório, único por usuário</li>
 *   <li>observadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 * </ul>
 */
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
