package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @Schema(description = "Email do usuário", example = "user@example.com")
    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "Formato de email inválido.")
    String email,

    @Schema(description = "Senha do usuário", example = "password123")
    @NotBlank(message = "A senha não pode ser vazia.")
    String password
) {}
