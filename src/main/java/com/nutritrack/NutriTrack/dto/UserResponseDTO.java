package com.nutritrack.dto;

import com.nutritrack.entity.NivelAtividade;
import com.nutritrack.entity.ObjetivoUsuario;
import com.nutritrack.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

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
