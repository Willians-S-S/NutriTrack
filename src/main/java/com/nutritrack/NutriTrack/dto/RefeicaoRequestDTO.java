package com.nutritrack.dto;

import com.nutritrack.entity.TipoRefeicao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public record RefeicaoRequestDTO(
    @Schema(description = "Tipo da refeição", example = "ALMOCO")
    @NotNull(message = "O tipo da refeição não pode ser nulo.")
    TipoRefeicao tipo,

    @Schema(description = "Data e hora da refeição (ISO-8601)", example = "2025-09-23T12:30:00Z")
    @NotNull(message = "A data e hora não podem ser nulas.")
    OffsetDateTime dataHora,

    @Schema(description = "Observações gerais sobre a refeição")
    String observacoes,

    @Schema(description = "Lista de itens da refeição")
    @NotEmpty(message = "A refeição deve conter pelo menos um item.")
    @Valid
    List<ItemRefeicaoRequestDTO> itens
) {}
