package com.nutritrack.dto;

import com.nutritrack.entity.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemRefeicaoResponseDTO(
    @Schema(description = "ID do item da refeição")
    UUID id,

    @Schema(description = "Alimento consumido")
    AlimentoResumidoDTO alimento,

    @Schema(description = "Quantidade consumida")
    BigDecimal quantidade,

    @Schema(description = "Unidade de medida")
    UnidadeMedida unidade,

    @Schema(description = "Observações sobre o item")
    String observacoes
) {}
