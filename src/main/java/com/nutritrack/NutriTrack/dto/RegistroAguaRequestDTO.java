package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

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
