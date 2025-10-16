package com.nutritrack.NutriTrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de autenticação de usuário.
 * Contém o email e a senha do usuário para login.
 *
 * Campos:
 * <ul>
 *   <li>email: Email do usuário, obrigatório e com formato válido</li>
 *   <li>password: Senha do usuário, obrigatória</li>
 * </ul>
 *
 * Exemplo de uso no Swagger UI:
 * {
 *   "email": "user@example.com",
 *   "password": "password123"
 * }
 */
public record AuthRequest(
    @Schema(description = "Email do usuário", example = "user@example.com")
    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "Formato de email inválido.")
    String email,

    @Schema(description = "Senha do usuário", example = "password123")
    @NotBlank(message = "A senha não pode ser vazia.")
    String password
) {}
