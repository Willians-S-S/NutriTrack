package com.nutritrack.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manipulador global de exceções para a API.
 * 
 * Intercepta exceções lançadas pelos controllers e gera respostas
 * padronizadas no formato {@link ErrorResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções do tipo {@link ResourceNotFoundException}.
     * Retorna status 404 NOT FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções do tipo {@link ConflictException}.
     * Retorna status 409 CONFLICT.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Trata exceções do tipo {@link BusinessException}.
     * Retorna status 422 UNPROCESSABLE ENTITY.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Trata exceções do tipo {@link AccessDeniedException}.
     * Retorna status 403 FORBIDDEN.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Trata erros de validação de argumentos em métodos de controllers.
     * Retorna status 400 BAD REQUEST com detalhes dos campos inválidos.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, List<String>> validationErrors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.groupingBy(
                fieldError -> fieldError.getField(),
                Collectors.mapping(fieldError -> fieldError.getDefaultMessage(), Collectors.toList())
            ));

        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Erro de validação",
            request.getDescription(false).substring(4),
            validationErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções genéricas não previstas.
     * Retorna status 500 INTERNAL SERVER ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            OffsetDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Ocorreu um erro inesperado no servidor.",
            request.getDescription(false).substring(4)
        );
        // Log da exceção para depuração em produção
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
