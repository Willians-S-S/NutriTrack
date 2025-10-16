package com.nutritrack.NutriTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de resumo diário de refeição.
 * Contém a data e os totais nutricionais consumidos no dia.
 *
 * Campos:
 * <ul>
 *   <li>data: Data do resumo</li>
 *   <li>totalCalorias: Total de calorias consumidas no dia</li>
 *   <li>totalProteinasG: Total de proteínas em gramas consumidas no dia</li>
 *   <li>totalCarboidratosG: Total de carboidratos em gramas consumidos no dia</li>
 *   <li>totalGordurasG: Total de gorduras em gramas consumidas no dia</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "data": "2025-09-24",
 *   "totalCalorias": 2000.0,
 *   "totalProteinasG": 75.0,
 *   "totalCarboidratosG": 250.0,
 *   "totalGordurasG": 70.0
 * }
 */
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
