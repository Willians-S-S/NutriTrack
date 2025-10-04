package com.nutritrack.NutriTrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando ocorre uma regra de negócio violada ou inválida.
 * 
 * Está mapeada para o status HTTP 422 (UNPROCESSABLE ENTITY).
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro personalizada.
     *
     * @param message Mensagem detalhando a violação da regra de negócio
     */
    public BusinessException(String message) {
        super(message);
    }
}
