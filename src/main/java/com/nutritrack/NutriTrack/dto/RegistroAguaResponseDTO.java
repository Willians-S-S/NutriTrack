package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RegistroAguaResponseDTO(
    @Schema(description = "ID do registro de água")
    UUID id,

    @Schema(description = "ID do usuário")
    UUID usuarioId,

    @Schema(description = "Quantidade de água em mililitros")
    Integer quantidadeMl,

    @Schema(description = "Data da medição")
    LocalDate dataMedicao,

    @Schema(description = "Data e hora da observação do registro")
    OffsetDateTime observadoEm
) {}
