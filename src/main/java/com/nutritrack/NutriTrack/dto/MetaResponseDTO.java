package com.nutritrack.NutriTrack.dto;

import com.nutritrack.NutriTrack.enums.TipoMeta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MetaResponseDTO(
        UUID id,
        UUID usuarioId,
        TipoMeta tipo,
        BigDecimal caloriasObjetivo,
        BigDecimal proteinasObjetivo,
        BigDecimal carboidratosObjetivo,
        BigDecimal gordurasObjetivo,
//        LocalDate dataInicio,
//        LocalDate dataFim,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {}
