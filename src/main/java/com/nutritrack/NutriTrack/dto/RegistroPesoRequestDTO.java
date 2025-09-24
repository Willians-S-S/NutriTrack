package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroPesoRequestDTO(
    @Schema(description = "Peso em quilogramas", example = "70.5")
    @NotNull(message = "O peso não pode ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O peso deve ser maior que zero.")
    BigDecimal pesoKg,

    @Schema(description = "Data da medição do peso", example = "2025-09-23")
    @NotNull(message = "A data da medição não pode ser nula.")
    @PastOrPresent(message = "A data da medição não pode ser no futuro.")
    LocalDate dataMedicao
) {}
