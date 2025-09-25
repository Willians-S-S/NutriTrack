package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * DTO resumido de um alimento.
 * Contém apenas informações básicas, como ID e nome, sem dados nutricionais.
 *
 * Campos:
 * <ul>
 *   <li>id: UUID único do alimento</li>
 *   <li>nome: Nome do alimento</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "id": "123e4567-e89b-12d3-a456-426614174000",
 *   "nome": "Maçã Gala"
 * }
 */
public record AlimentoResumidoDTO(
    @Schema(description = "ID do alimento")
    UUID id,

    @Schema(description = "Nome do alimento")
    String nome
) {}
