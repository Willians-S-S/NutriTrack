package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de resposta para um registro de consumo de água.
 * Contém informações sobre a quantidade consumida, data da medição e usuário.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID do registro de água</li>
 *   <li>usuarioId: UUID do usuário dono do registro</li>
 *   <li>quantidadeMl: Quantidade de água consumida em mililitros</li>
 *   <li>dataMedicao: Data da medição do consumo de água</li>
 *   <li>observadoEm: Data e hora em que o registro foi criado ou atualizado</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "id": "456e7890-e89b-12d3-a456-426614174000",
 *   "usuarioId": "789e0123-e89b-12d3-a456-426614174000",
 *   "quantidadeMl": 250,
 *   "dataMedicao": "2025-09-23",
 *   "observadoEm": "2025-09-23T12:31:00Z"
 * }
 */
public record RegistroAguaResponseDTO(
    @Schema(description = "ID do registro de água")
    UUID id,

    @Schema(description = "ID do usuário")
    UUID usuarioId,

    @Schema(description = "Quantidade de água em mililitros")
    Integer quantidadeMl,

    @Schema(description = "Data da medição")
    LocalDate dataMedicao,

    @Schema(description = "Data e hora da observação do registro")
    OffsetDateTime observadoEm
) {}
