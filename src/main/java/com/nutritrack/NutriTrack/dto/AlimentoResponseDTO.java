package com.nutritrack.NutriTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de resposta para informações de um alimento.
 * Contém dados nutricionais e informações de auditoria do registro.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do alimento</li>
 *   <li>nome: Nome do alimento</li>
 *   <li>calorias: Calorias por porção base</li>
 *   <li>proteinasG: Proteínas em gramas por porção base</li>
 *   <li>carboidratosG: Carboidratos em gramas por porção base</li>
 *   <li>gordurasG: Gorduras em gramas por porção base</li>
 *   <li>criadoEm: Data e hora de criação do registro</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "id": "123e4567-e89b-12d3-a456-426614174000",
 *   "nome": "Maçã Gala",
 *   "calorias": 52.1,
 *   "proteinasG": 0.3,
 *   "carboidratosG": 13.8,
 *   "gordurasG": 0.2,
 *   "criadoEm": "2025-09-24T23:00:00Z"
 * }
 */
public record AlimentoResponseDTO(
    @Schema(description = "ID único do alimento")
    UUID id,

    @Schema(description = "Nome do alimento")
    String nome,

    @Schema(description = "Calorias por porção base")
    BigDecimal calorias,

    @Schema(description = "Proteínas em gramas por porção base")
    BigDecimal proteinasG,

    @Schema(description = "Carboidratos em gramas por porção base")
    BigDecimal carboidratosG,

    @Schema(description = "Gorduras em gramas por porção base")
    BigDecimal gordurasG,

    @Schema(description = "Data de criação do registro")
    OffsetDateTime criadoEm
) {}
