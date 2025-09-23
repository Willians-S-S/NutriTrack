package com.nutritrack.dto;

import com.nutritrack.entity.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemRefeicaoRequestDTO(
    @Schema(description = "ID do alimento a ser adicionado", required = true)
    @NotNull(message = "O ID do alimento não pode ser nulo.")
    UUID alimentoId,

    @Schema(description = "Quantidade do alimento", example = "150.0")
    @NotNull(message = "A quantidade não pode ser nula.")
    @DecimalMin(value = "0.0", inclusive = false, message = "A quantidade deve ser maior que zero.")
    BigDecimal quantidade,

    @Schema(description = "Unidade de medida da quantidade", example = "GRAMA")
    @NotNull(message = "A unidade não pode ser nula.")
    UnidadeMedida unidade,

    @Schema(description = "Observações sobre o item", example = "Com pouco sal")
    String observacoes
) {}
