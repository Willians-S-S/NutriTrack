package com.nutritrack.dto;

import com.nutritrack.entity.NivelAtividade;
import com.nutritrack.entity.ObjetivoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserProfileUpdateDTO(
    @Schema(description = "Nova altura do usuário em metros", example = "1.76")
    @NotNull(message = "A altura não pode ser nula.")
    @DecimalMin(value = "0.5", message = "A altura deve ser maior que 0.5 metros.")
    BigDecimal alturaM,

    @Schema(description = "Novo nível de atividade física do usuário", example = "LEVE")
    @NotNull(message = "O nível de atividade não pode ser nulo.")
    NivelAtividade nivelAtividade,

    @Schema(description = "Novo objetivo do usuário", example = "MANTER_PESO")
    @NotNull(message = "O objetivo do usuário não pode ser nulo.")
    ObjetivoUsuario objetivoUsuario
) {}
