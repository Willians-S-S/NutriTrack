package com.nutritrack.NutriTrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade que representa um item de uma refeição.
 * Cada item refere-se a um alimento específico consumido em determinada quantidade e unidade de medida.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do item da refeição (chave primária)</li>
 *   <li>refeicao: Referência à refeição à qual este item pertence (ManyToOne, obrigatório)</li>
 *   <li>alimento: Referência ao alimento consumido (ManyToOne, obrigatório, Eager fetch)</li>
 *   <li>quantidade: Quantidade do alimento consumido, obrigatória</li>
 *   <li>unidade: Unidade de medida da quantidade (GRAMAS, ML etc.), obrigatória</li>
 *   <li>observacoes: Observações adicionais sobre o consumo do item</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itens_refeicao")
public class ItemRefeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_refeicao", nullable = false)
    private Refeicao refeicao;

    @ManyToOne(fetch = FetchType.EAGER) // Eager para ter os dados do alimento sempre disponíveis
    @JoinColumn(name = "id_alimento", nullable = false)
    private Alimento alimento;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnidadeMedida unidade;

    private String observacoes;
}
