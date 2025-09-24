package com.nutritrack.dto;

import com.nutritrack.entity.TipoRefeicao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RefeicaoResponseDTO(
    @Schema(description = "ID da refeição")
    UUID id,

    @Schema(description = "ID do usuário proprietário da refeição")
    UUID usuarioId,

    @Schema(description = "Tipo da refeição")
    TipoRefeicao tipo,

    @Schema(description = "Data e hora da refeição")
    OffsetDateTime dataHora,

    @Schema(description = "Observações gerais sobre a refeição")
    String observacoes,

    @Schema(description = "Data de criação do registro")
    OffsetDateTime criadoEm,

    @Schema(description = "Lista de itens da refeição")
    List<ItemRefeicaoResponseDTO> itens,

    @Schema(description = "Total de calorias da refeição")
    BigDecimal totalCalorias,

    @Schema(description = "Total de proteínas da refeição")
    BigDecimal totalProteinasG,

    @Schema(description = "Total de carboidratos da refeição")
    BigDecimal totalCarboidratosG,

    @Schema(description = "Total de gorduras da refeição")
    BigDecimal totalGordurasG
) {}
