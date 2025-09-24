package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RegistroAguaDiarioDTO(
    @Schema(description = "Data do resumo")
    LocalDate data,

    @Schema(description = "Total de água consumida no dia em mililitros")
    Integer totalQuantidadeMl
) {}
