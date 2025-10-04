package com.nutritrack.NutriTrack.dto;

import com.nutritrack.NutriTrack.entity.TipoRefeicao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO para requisição de criação ou atualização de uma refeição.
 * Contém tipo da refeição, data/hora, observações e lista de itens.
 *
 * Campos:
 * <ul>
 *   <li>tipo: Tipo da refeição (ex: CAFÉ_DA_MANHÃ, ALMOÇO, JANTAR), obrigatório</li>
 *   <li>dataHora: Data e hora da refeição (ISO-8601), obrigatório</li>
 *   <li>observacoes: Observações gerais sobre a refeição, opcional</li>
 *   <li>itens: Lista de itens da refeição, obrigatória e com pelo menos um item ({@link ItemRefeicaoRequestDTO})</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "tipo": "ALMOCO",
 *   "dataHora": "2025-09-23T12:30:00Z",
 *   "observacoes": "Refeição leve",
 *   "itens": [
 *     {
 *       "alimentoId": "123e4567-e89b-12d3-a456-426614174000",
 *       "quantidade": 150.0,
 *       "unidade": "GRAMA",
 *       "observacoes": "Com pouco sal"
 *     }
 *   ]
 * }
 */
public record RefeicaoRequestDTO(
    @Schema(description = "Tipo da refeição", example = "ALMOCO")
    @NotNull(message = "O tipo da refeição não pode ser nulo.")
    TipoRefeicao tipo,

    @Schema(description = "Data e hora da refeição (ISO-8601)", example = "2025-09-23T12:30:00Z")
    @NotNull(message = "A data e hora não podem ser nulas.")
    OffsetDateTime dataHora,

    @Schema(description = "Observações gerais sobre a refeição")
    String observacoes,

    @Schema(description = "Lista de itens da refeição")
    @NotEmpty(message = "A refeição deve conter pelo menos um item.")
    @Valid
    List<ItemRefeicaoRequestDTO> itens
) {}
