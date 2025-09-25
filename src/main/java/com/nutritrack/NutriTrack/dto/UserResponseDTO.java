package com.nutritrack.dto;

import com.nutritrack.entity.NivelAtividade;
import com.nutritrack.entity.ObjetivoUsuario;
import com.nutritrack.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de resposta para informações do usuário.
 * Contém os dados principais do perfil do usuário e informações de auditoria.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do usuário</li>
 *   <li>nome: Nome completo do usuário</li>
 *   <li>email: Email do usuário</li>
 *   <li>alturaM: Altura em metros</li>
 *   <li>dataNascimento: Data de nascimento do usuário</li>
 *   <li>nivelAtividade: Nível de atividade física do usuário</li>
 *   <li>objetivoUsuario: Objetivo do usuário</li>
 *   <li>role: Perfil de acesso do usuário</li>
 *   <li>criadoEm: Data de criação do registro</li>
 *   <li>atualizadoEm: Data da última atualização do registro</li>
 * </ul>
 */
public record UserResponseDTO(
    @Schema(description = "ID único do usuário")
    UUID id,

    @Schema(description = "Nome do usuário")
    String nome,

    @Schema(description = "Email do usuário")
    String email,

    @Schema(description = "Altura em metros")
    BigDecimal alturaM,

    @Schema(description = "Data de nascimento")
    LocalDate dataNascimento,

    @Schema(description = "Nível de atividade")
    NivelAtividade nivelAtividade,

    @Schema(description = "Objetivo do usuário")
    ObjetivoUsuario objetivoUsuario,

    @Schema(description = "Perfil de acesso")
    Role role,

    @Schema(description = "Data de criação do registro")
    OffsetDateTime criadoEm,

    @Schema(description = "Data da última atualização")
    OffsetDateTime atualizadoEm
) {}
