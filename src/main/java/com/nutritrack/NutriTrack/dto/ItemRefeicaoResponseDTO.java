package com.nutritrack.dto;

import com.nutritrack.entity.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de resposta para um item de refeição.
 * Contém informações do alimento consumido, quantidade, unidade de medida e observações.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID do item da refeição</li>
 *   <li>alimento: Dados resumidos do alimento consumido ({@link AlimentoResumidoDTO})</li>
 *   <li>quantidade: Quantidade consumida do alimento</li>
 *   <li>unidade: Unidade de medida da quantidade</li>
 *   <li>observacoes: Observações adicionais sobre o item, se houver</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "id": "456e7890-e89b-12d3-a456-426614174000",
 *   "alimento": {
 *       "id": "123e4567-e89b-12d3-a456-426614174000",
 *       "nome": "Maçã Gala"
 *   },
 *   "quantidade": 150.0,
 *   "unidade": "GRAMA",
 *   "observacoes": "Com pouco sal"
 * }
 */
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
