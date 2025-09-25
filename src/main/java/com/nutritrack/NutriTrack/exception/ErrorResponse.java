package com.nutritrack.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO para padronizar respostas de erro da API.
 * Contém informações sobre o erro ocorrido, incluindo status HTTP,
 * mensagem, caminho da requisição e detalhes de validação.
 *
 * Campos:
 * <ul>
 *   <li>timestamp: Data e hora do erro</li>
 *   <li>status: Código de status HTTP</li>
 *   <li>error: Nome do erro HTTP</li>
 *   <li>message: Mensagem de erro principal</li>
 *   <li>path: Caminho da requisição que gerou o erro</li>
 *   <li>validationErrors: Detalhes de erros de validação (opcional)</li>
 * </ul>
 */
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
    /**
     * Construtor alternativo para erros gerais, sem detalhes de validação.
     *
     * @param timestamp Data e hora do erro
     * @param status Código de status HTTP
     * @param error Nome do erro HTTP
     * @param message Mensagem de erro principal
     * @param path Caminho da requisição que gerou o erro
     */
    public ErrorResponse(OffsetDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
