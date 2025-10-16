package com.nutritrack.NutriTrack.dto;

import com.nutritrack.NutriTrack.entity.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para requisição de criação ou atualização de um item de refeição.
 * Contém informações do alimento, quantidade, unidade de medida e observações opcionais.
 *
 * Campos:
 * <ul>
 *   <li>alimentoId: UUID do alimento a ser adicionado, obrigatório</li>
 *   <li>quantidade: Quantidade do alimento, obrigatória e maior que zero</li>
 *   <li>unidade: Unidade de medida da quantidade, obrigatória (ex: GRAMA, ML)</li>
 *   <li>observacoes: Observações adicionais sobre o item, opcional</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "alimentoId": "123e4567-e89b-12d3-a456-426614174000",
 *   "quantidade": 150.0,
 *   "unidade": "GRAMA",
 *   "observacoes": "Com pouco sal"
 * }
 */
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
