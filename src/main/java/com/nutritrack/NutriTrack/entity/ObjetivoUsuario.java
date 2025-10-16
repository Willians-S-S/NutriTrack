package com.nutritrack.NutriTrack.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum que representa os objetivos do usuário em relação ao peso.
 *
 * Valores:
 * <ul>
 *   <li>PERDER_PESO: Objetivo de reduzir o peso corporal</li>
 *   <li>MANTER_PESO: Objetivo de manter o peso atual</li>
 *   <li>GANHAR_PESO: Objetivo de aumentar o peso corporal</li>
 *   <li>PERFORMANCE: Objetivo de melhorar a performance esportiva</li>
 *   <li>SAUDE: Objetivo de melhorar a saúde em geral</li>
 * </ul>
 */
public enum ObjetivoUsuario {
    PERDER_PESO,
    MANTER_PESO,
    GANHAR_PESO,
    PERFORMANCE,
    SAUDE;

    @JsonCreator
    public static ObjetivoUsuario fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ObjetivoUsuario objetivo : ObjetivoUsuario.values()) {
            if (objetivo.name().equalsIgnoreCase(value)) {
                return objetivo;
            }
        }
        throw new IllegalArgumentException("Objetivo de usuário inválido: " + value);
    }
}
