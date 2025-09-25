package com.nutritrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando ocorre um conflito no estado do recurso,
 * como tentativa de criar um recurso que já existe.
 * 
 * Está mapeada para o status HTTP 409 (CONFLICT).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro personalizada.
     *
     * @param message Mensagem detalhando o conflito ocorrido
     */
    public ConflictException(String message) {
        super(message);
    }
}
