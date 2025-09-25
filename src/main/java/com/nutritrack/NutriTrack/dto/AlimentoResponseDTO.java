package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AlimentoResponseDTO(
    @Schema(description = "ID único do alimento")
    UUID id,

    @Schema(description = "Nome do alimento")
    String nome,

    @Schema(description = "Calorias por porção base")
    BigDecimal calorias,

    @Schema(description = "Proteínas em gramas por porção base")
    BigDecimal proteinasG,

    @Schema(description = "Carboidratos em gramas por porção base")
    BigDecimal carboidratosG,

    @Schema(description = "Gorduras em gramas por porção base")
    BigDecimal gordurasG,

    @Schema(description = "Data de criação do registro")
    OffsetDateTime criadoEm
) {}
