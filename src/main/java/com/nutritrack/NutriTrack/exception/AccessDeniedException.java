package com.nutritrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um usuário tenta acessar um recurso
 * para o qual não possui permissão adequada.
 * 
 * Está mapeada para o status HTTP 403 (FORBIDDEN).
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro personalizada.
     *
     * @param message Mensagem detalhando o motivo do acesso negado
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
