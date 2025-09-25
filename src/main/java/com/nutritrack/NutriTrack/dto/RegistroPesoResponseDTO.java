package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de resposta para um registro de peso.
 * Contém informações sobre o valor do peso, data da medição e usuário.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID do registro de peso</li>
 *   <li>usuarioId: UUID do usuário dono do registro</li>
 *   <li>pesoKg: Peso registrado em quilogramas</li>
 *   <li>dataMedicao: Data da medição do peso</li>
 *   <li>observadoEm: Data e hora em que o registro foi criado ou atualizado</li>
 * </ul>
 */
public record RegistroPesoResponseDTO(
    @Schema(description = "ID do registro de peso")
    UUID id,

    @Schema(description = "ID do usuário")
    UUID usuarioId,

    @Schema(description = "Peso em quilogramas")
    BigDecimal pesoKg,

    @Schema(description = "Data da medição")
    LocalDate dataMedicao,

    @Schema(description = "Data e hora da observação do registro")
    OffsetDateTime observadoEm
) {}
