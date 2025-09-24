package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RegistroPesoResponseDTO(
    @Schema(description = "ID do registro de peso")
    UUID id,

    @Schema(description = "ID do usuário")
    UUID usuarioId,

    @Schema(description = "Peso em quilogramas")
    BigDecimal pesoKg,

    @Schema(description = "Data da medição")
    LocalDate dataMedicao,

    @Schema(description = "Data e hora da observação do registro")
    OffsetDateTime observadoEm
) {}
