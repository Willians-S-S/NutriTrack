package com.nutritrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa uma refeição de um usuário.
 * Contém informações sobre o tipo de refeição, data/hora, observações e seus itens consumidos.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único da refeição (chave primária)</li>
 *   <li>usuario: Referência ao usuário dono da refeição (ManyToOne, obrigatório)</li>
 *   <li>tipo: Tipo da refeição (café da manhã, almoço, etc.), obrigatório</li>
 *   <li>dataHora: Data e hora em que a refeição ocorreu, obrigatório</li>
 *   <li>observacoes: Observações gerais sobre a refeição</li>
 *   <li>criadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 *   <li>itens: Lista de itens consumidos na refeição (OneToMany, EAGER fetch, cascata ALL)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refeicoes")
public class Refeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_refeicao")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRefeicao tipo;

    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private OffsetDateTime criadoEm;

    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemRefeicao> itens;
}
