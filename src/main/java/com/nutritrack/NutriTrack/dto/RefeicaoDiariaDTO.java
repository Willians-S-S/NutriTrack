package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RefeicaoDiariaDTO(
    @Schema(description = "Data do resumo")
    LocalDate data,

    @Schema(description = "Total de calorias consumidas no dia")
    BigDecimal totalCalorias,

    @Schema(description = "Total de proteínas consumidas no dia")
    BigDecimal totalProteinasG,

    @Schema(description = "Total de carboidratos consumidos no dia")
    BigDecimal totalCarboidratosG,

    @Schema(description = "Total de gorduras consumidas no dia")
    BigDecimal totalGordurasG
) {}
