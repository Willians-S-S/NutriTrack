package com.nutritrack.dto;

import com.nutritrack.entity.TipoRefeicao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de resposta para uma refeição.
 * Contém informações completas da refeição, incluindo tipo, data/hora, observações,
 * lista de itens e totais nutricionais.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID da refeição</li>
 *   <li>usuarioId: UUID do usuário proprietário da refeição</li>
 *   <li>tipo: Tipo da refeição (ex: CAFÉ_DA_MANHÃ, ALMOÇO, JANTAR)</li>
 *   <li>dataHora: Data e hora da refeição</li>
 *   <li>observacoes: Observações gerais sobre a refeição</li>
 *   <li>criadoEm: Data e hora de criação do registro</li>
 *   <li>itens: Lista de itens da refeição ({@link ItemRefeicaoResponseDTO})</li>
 *   <li>totalCalorias: Total de calorias da refeição</li>
 *   <li>totalProteinasG: Total de proteínas em gramas da refeição</li>
 *   <li>totalCarboidratosG: Total de carboidratos em gramas da refeição</li>
 *   <li>totalGordurasG: Total de gorduras em gramas da refeição</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "id": "456e7890-e89b-12d3-a456-426614174000",
 *   "usuarioId": "789e0123-e89b-12d3-a456-426614174000",
 *   "tipo": "ALMOCO",
 *   "dataHora": "2025-09-23T12:30:00Z",
 *   "observacoes": "Refeição leve",
 *   "criadoEm": "2025-09-23T12:31:00Z",
 *   "itens": [
 *     {
 *       "id": "111e2222-e89b-12d3-a456-426614174000",
 *       "alimento": {
 *           "id": "123e4567-e89b-12d3-a456-426614174000",
 *           "nome": "Maçã Gala"
 *       },
 *       "quantidade": 150.0,
 *       "unidade": "GRAMA",
 *       "observacoes": "Com pouco sal"
 *     }
 *   ],
 *   "totalCalorias": 200.0,
 *   "totalProteinasG": 0.3,
 *   "totalCarboidratosG": 13.8,
 *   "totalGordurasG": 0.2
 * }
 */
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
