package com.nutritrack.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
    @Schema(description = "Timestamp do erro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime timestamp,

    @Schema(description = "Código de status HTTP")
    int status,

    @Schema(description = "Nome do erro HTTP")
    String error,

    @Schema(description = "Mensagem de erro principal")
    String message,

    @Schema(description = "Caminho da requisição que gerou o erro")
    String path,

    @Schema(description = "Detalhes de erros de validação", nullable = true)
    Map<String, List<String>> validationErrors
) {
    // Construtor para erros gerais
    public ErrorResponse(OffsetDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
