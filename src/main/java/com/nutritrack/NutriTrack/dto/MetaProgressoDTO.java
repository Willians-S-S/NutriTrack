package com.nutritrack.NutriTrack.dto;

import java.math.BigDecimal;

public record MetaProgressoDTO(
    ProgressoItem calorias,
    ProgressoItem proteinas,
    ProgressoItem carboidratos,
    ProgressoItem gorduras
) {
    public record ProgressoItem(
        BigDecimal objetivo,
        BigDecimal consumido,
        BigDecimal porcentagem
    ) {}
}
