package com.nutritrack.dto;

import com.nutritrack.entity.NivelAtividade;
import com.nutritrack.entity.ObjetivoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO para atualização do perfil do usuário.
 * Contém informações que podem ser alteradas no perfil: altura, nível de atividade e objetivo.
 *
 * Campos:
 * <ul>
 *   <li>alturaM: Altura do usuário em metros, obrigatória e maior que 0.5</li>
 *   <li>nivelAtividade: Nível de atividade física do usuário, obrigatório</li>
 *   <li>objetivoUsuario: Objetivo do usuário, obrigatório</li>
 * </ul>
 */
public record UserProfileUpdateDTO(
    @Schema(description = "Nova altura do usuário em metros")
    @NotNull(message = "A altura não pode ser nula.")
    @DecimalMin(value = "0.5", message = "A altura deve ser maior que 0.5 metros.")
    BigDecimal alturaM,

    @Schema(description = "Novo nível de atividade física do usuário")
    @NotNull(message = "O nível de atividade não pode ser nulo.")
    NivelAtividade nivelAtividade,

    @Schema(description = "Novo objetivo do usuário")
    @NotNull(message = "O objetivo do usuário não pode ser nulo.")
    ObjetivoUsuario objetivoUsuario
) {}
