package com.nutritrack.dto;

import com.nutritrack.entity.NivelAtividade;
import com.nutritrack.entity.ObjetivoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRequestDTO(
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres.")
    String nome,

    @Schema(description = "Email único do usuário", example = "joao.silva@example.com")
    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "Formato de email inválido.")
    String email,

    @Schema(description = "Senha do usuário (mínimo 8 caracteres)", example = "senhaForte123")
    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    String senha,

    @Schema(description = "Altura do usuário em metros", example = "1.75")
    @NotNull(message = "A altura não pode ser nula.")
    @DecimalMin(value = "0.5", message = "A altura deve ser maior que 0.5 metros.")
    BigDecimal alturaM,

    @Schema(description = "Data de nascimento do usuário (formato YYYY-MM-DD)", example = "1990-01-15")
    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser no passado.")
    LocalDate dataNascimento,

    @Schema(description = "Nível de atividade física do usuário", example = "MODERADO")
    @NotNull(message = "O nível de atividade não pode ser nulo.")
    NivelAtividade nivelAtividade,

    @Schema(description = "Objetivo do usuário", example = "PERDER_PESO")
    @NotNull(message = "O objetivo do usuário não pode ser nulo.")
    ObjetivoUsuario objetivoUsuario
) {}
