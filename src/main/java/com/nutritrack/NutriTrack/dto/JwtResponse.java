package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para autenticação.
 * Contém o token JWT gerado após login bem-sucedido.
 *
 * Campos:
 * <ul>
 *   <li>token: Token de autenticação JWT</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 */
public record JwtResponse(
    @Schema(description = "Token de autenticação JWT")
    String token
) {}
