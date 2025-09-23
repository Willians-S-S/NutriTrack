package com.nutritrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AlimentoRequestDTO(
    @Schema(description = "Nome do alimento", example = "Maçã Gala")
    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(max = 160, message = "O nome não pode exceder 160 caracteres.")
    String nome,

    @Schema(description = "Calorias por porção base", example = "52.1")
    @NotNull(message = "As calorias não podem ser nulas.")
    @DecimalMin(value = "0.0", inclusive = false, message = "As calorias devem ser maiores que zero.")
    BigDecimal calorias,

    @Schema(description = "Proteínas em gramas por porção base", example = "0.3")
    @NotNull(message = "As proteínas não podem ser nulas.")
    @DecimalMin(value = "0.0", message = "As proteínas não podem ser negativas.")
    BigDecimal proteinasG,

    @Schema(description = "Carboidratos em gramas por porção base", example = "13.8")
    @NotNull(message = "Os carboidratos não podem ser nulos.")
    @DecimalMin(value = "0.0", message = "Os carboidratos não podem ser negativos.")
    BigDecimal carboidratosG,

    @Schema(description = "Gorduras em gramas por porção base", example = "0.2")
    @NotNull(message = "As gorduras não podem ser nulas.")
    @DecimalMin(value = "0.0", message = "As gorduras não podem ser negativas.")
    BigDecimal gordurasG
) {}
