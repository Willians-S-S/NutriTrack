package com.nutritrack.NutriTrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um usuário do sistema.
 * Contém informações pessoais, dados de perfil e relacionamentos com registros de peso, água e refeições.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do usuário (chave primária)</li>
 *   <li>nome: Nome completo do usuário, obrigatório</li>
 *   <li>email: Email único do usuário, obrigatório</li>
 *   <li>senhaHash: Hash da senha do usuário, obrigatório</li>
 *   <li>alturaM: Altura do usuário em metros, obrigatório</li>
 *   <li>dataNascimento: Data de nascimento do usuário, obrigatório</li>
 *   <li>nivelAtividade: Nível de atividade física do usuário, obrigatório</li>
 *   <li>objetivoUsuario: Objetivo do usuário (perder, manter ou ganhar peso), obrigatório</li>
 *   <li>role: Perfil de acesso do usuário (ROLE_USER, ROLE_ADMIN), obrigatório</li>
 *   <li>criadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 *   <li>atualizadoEm: Data e hora da última atualização do registro, atualizada automaticamente</li>
 *   <li>refeicoes: Lista de refeições do usuário (OneToMany, cascade ALL, orphanRemoval)</li>
 *   <li>registrosPeso: Lista de registros de peso do usuário (OneToMany, cascade ALL, orphanRemoval)</li>
 *   <li>registrosAgua: Lista de registros de consumo de água do usuário (OneToMany, cascade ALL, orphanRemoval)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "altura_m", nullable = false, precision = 4, scale = 2)
    private BigDecimal alturaM;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_atividade", nullable = false)
    private NivelAtividade nivelAtividade;

    @Enumerated(EnumType.STRING)
    @Column(name = "objetivo_usuario", nullable = false)
    private ObjetivoUsuario objetivoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private OffsetDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refeicao> refeicoes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroPeso> registrosPeso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAgua> registrosAgua;
}
