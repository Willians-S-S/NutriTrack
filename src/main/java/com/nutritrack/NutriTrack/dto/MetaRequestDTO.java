package com.nutritrack.NutriTrack.dto;

import com.nutritrack.NutriTrack.enums.TipoMeta;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaRequestDTO(
        @NotNull TipoMeta tipo,
        @NotNull BigDecimal caloriasObjetivo,
        @NotNull BigDecimal proteinasObjetivo,
        @NotNull BigDecimal carboidratosObjetivo,
        @NotNull BigDecimal gordurasObjetivo,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim
) {}
