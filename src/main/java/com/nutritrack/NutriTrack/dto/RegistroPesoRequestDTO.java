package com.nutritrack.NutriTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para requisição de registro de peso.
 * Contém o valor do peso em quilogramas e a data da medição.
 *
 * Campos:
 * <ul>
 *   <li>pesoKg: Peso em quilogramas, obrigatório e maior que zero</li>
 *   <li>dataMedicao: Data da medição do peso, obrigatória e não pode ser no futuro</li>
 * </ul>
 */
public record RegistroPesoRequestDTO(
    @Schema(description = "Peso em quilogramas")
    @NotNull(message = "O peso não pode ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O peso deve ser maior que zero.")
    BigDecimal pesoKg,

    @Schema(description = "Data da medição do peso")
    @NotNull(message = "A data da medição não pode ser nula.")
    @PastOrPresent(message = "A data da medição não pode ser no futuro.")
    LocalDate dataMedicao
) {}
