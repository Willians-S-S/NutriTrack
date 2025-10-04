package com.nutritrack.NutriTrack.entity;

/**
 * Enum que representa os perfis de acesso de um usuário no sistema.
 *
 * Valores:
 * <ul>
 *   <li>ROLE_USER: Usuário comum, acesso padrão ao sistema</li>
 *   <li>ROLE_ADMIN: Usuário administrador, acesso completo às funcionalidades</li>
 * </ul>
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
