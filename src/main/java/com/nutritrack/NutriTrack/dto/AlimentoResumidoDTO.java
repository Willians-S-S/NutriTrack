package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AlimentoResumidoDTO(
    @Schema(description = "ID do alimento")
    UUID id,

    @Schema(description = "Nome do alimento")
    String nome
) {}
