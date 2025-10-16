package com.nutritrack.NutriTrack.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum que representa os níveis de atividade física de um usuário.
 *
 * Valores:
 * <ul>
 *   <li>SEDENTARIO: Sem atividade física regular</li>
 *   <li>LEVE: Atividade física leve, ocasional</li>
 *   <li>MODERADO: Atividade física moderada, regular</li>
 *   <li>ALTO: Atividade física intensa, regular</li>
 *   <li>ATLETA: Atleta, treino intenso diário</li>
 * </ul>
 */
public enum NivelAtividade {
    SEDENTARIO,
    LEVE,
    MODERADO,
    ALTO,
    ATLETA;

    @JsonCreator
    public static NivelAtividade fromString(String value) {
        if (value == null) {
            return null;
        }
        for (NivelAtividade nivel : NivelAtividade.values()) {
            if (nivel.name().equalsIgnoreCase(value)) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nível de atividade inválido: " + value);
    }
}
