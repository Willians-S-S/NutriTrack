package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO para requisição de registro de consumo de água.
 * Contém a quantidade de água consumida e a data da medição.
 *
 * Campos:
 * <ul>
 *   <li>quantidadeMl: Quantidade de água consumida em mililitros, obrigatória e mínima de 1 ml</li>
 *   <li>dataMedicao: Data da medição do consumo de água, obrigatória e não pode ser no futuro</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "quantidadeMl": 250,
 *   "dataMedicao": "2025-09-23"
 * }
 */
public record RegistroAguaRequestDTO(
    @Schema(description = "Quantidade de água em mililitros", example = "250")
    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1 ml.")
    Integer quantidadeMl,

    @Schema(description = "Data da medição do consumo de água", example = "2025-09-23")
    @NotNull(message = "A data da medição não pode ser nula.")
    @PastOrPresent(message = "A data da medição não pode ser no futuro.")
    LocalDate dataMedicao
) {}
