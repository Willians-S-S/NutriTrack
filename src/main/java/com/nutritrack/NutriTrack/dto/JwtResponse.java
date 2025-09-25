package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record JwtResponse(
    @Schema(description = "Token de autenticação JWT")
    String token
) {}
