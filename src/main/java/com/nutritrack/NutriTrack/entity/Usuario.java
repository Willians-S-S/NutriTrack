package com.nutritrack.NutriTrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um usuário no sistema.
 * Implementa a interface UserDetails do Spring Security para integração com autenticação.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do usuário (chave primária)</li>
 *   <li>nome: Nome completo do usuário, obrigatório</li>
 *   <li>email: Email do usuário, obrigatório e único, usado como username</li>
 *   <li>senhaHash: Hash da senha do usuário, obrigatório</li>
 *   <li>alturaM: Altura do usuário em metros, obrigatória</li>
 *   <li>dataNascimento: Data de nascimento do usuário, obrigatória</li>
 *   <li>nivelAtividade: Nível de atividade física do usuário, obrigatório</li>
 *   <li>objetivoUsuario: Objetivo do usuário em relação ao peso, obrigatório</li>
 *   <li>role: Perfil de acesso do usuário (USER ou ADMIN), obrigatório</li>
 *   <li>criadoEm: Data e hora de criação do registro, preenchida automaticamente</li>
 *   <li>atualizadoEm: Data e hora da última atualização do registro, preenchida automaticamente</li>
 *   <li>refeicoes: Lista de refeições do usuário (OneToMany)</li>
 *   <li>registrosPeso: Lista de registros de peso do usuário (OneToMany)</li>
 *   <li>registrosAgua: Lista de registros de consumo de água do usuário (OneToMany)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

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

    @Convert(converter = com.nutritrack.NutriTrack.entity.converter.NivelAtividadeConverter.class)
    @Column(name = "nivel_atividade", nullable = false)
    private NivelAtividade nivelAtividade;

    @Convert(converter = com.nutritrack.NutriTrack.entity.converter.ObjetivoUsuarioConverter.class)
    @Column(name = "objetivo", nullable = false)
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

    // Relacionamentos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Refeicao> refeicoes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroPeso> registrosPeso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroAgua> registrosAgua;

    // Métodos da interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
