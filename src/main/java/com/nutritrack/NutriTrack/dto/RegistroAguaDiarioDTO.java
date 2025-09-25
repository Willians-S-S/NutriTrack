package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * DTO de resumo diário de consumo de água.
 * Contém a data e a quantidade total de água consumida no dia.
 *
 * Campos:
 * <ul>
 *   <li>data: Data do resumo</li>
 *   <li>totalQuantidadeMl: Quantidade total de água consumida no dia, em mililitros</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "data": "2025-09-24",
 *   "totalQuantidadeMl": 2000
 * }
 */
public record RegistroAguaDiarioDTO(
    @Schema(description = "Data do resumo")
    LocalDate data,

    @Schema(description = "Total de água consumida no dia em mililitros")
    Integer totalQuantidadeMl
) {}
