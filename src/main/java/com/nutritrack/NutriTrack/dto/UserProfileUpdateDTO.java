package com.nutritrack.NutriTrack.dto;

import com.nutritrack.NutriTrack.entity.NivelAtividade;
import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO para atualização do perfil do usuário.
 * Contém informações que podem ser alteradas no perfil: altura, nível de atividade e objetivo.
 *
 * Campos:
 * <ul>
 *   <li>alturaM: Altura do usuário em metros, obrigatória e maior que 0.5</li>
 *   <li>nivelAtividade: Nível de atividade física do usuário (SEDENTARIO, LEVE, MODERADO, ALTO, ATLETA)</li>
 *   <li>objetivoUsuario: Objetivo do usuário (PERDER_PESO, MANTER_PESO, GANHAR_PESO, PERFORMANCE, SAUDE)</li>
 * </ul>
 */
public record UserProfileUpdateDTO(
    @Schema(description = "Novo nome do usuário")
    String nome,

    @Schema(description = "Nova altura do usuário em metros")
    @DecimalMin(value = "0.5", message = "A altura deve ser maior que 0.5 metros.")
    BigDecimal alturaM,

    @Schema(description = "Novo peso do usuário em kg")
    @DecimalMin(value = "20.0", message = "O peso deve ser maior que 20.0 kg.")
    BigDecimal peso,

    @Schema(description = "Novo nível de atividade física do usuário")
    NivelAtividade nivelAtividade,

    @Schema(description = "Novo objetivo do usuário")
    ObjetivoUsuario objetivoUsuario
) {}
