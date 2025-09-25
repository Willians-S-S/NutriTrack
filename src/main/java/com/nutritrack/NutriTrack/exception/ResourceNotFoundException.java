package com.nutritrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um recurso não é encontrado.
 * 
 * Esta exceção retorna status HTTP 404 (NOT FOUND) para o cliente.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro.
     * 
     * @param message Mensagem explicativa sobre o recurso não encontrado
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
