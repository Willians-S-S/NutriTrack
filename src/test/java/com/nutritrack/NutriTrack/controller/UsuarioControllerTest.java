package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.config.AuthorizationConfig;
import com.nutritrack.NutriTrack.config.SecurityConfig;
import com.nutritrack.NutriTrack.dto.UserProfileUpdateDTO;
import com.nutritrack.NutriTrack.dto.UserResponseDTO;
import com.nutritrack.NutriTrack.entity.NivelAtividade;
import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import com.nutritrack.NutriTrack.entity.Role;
import com.nutritrack.NutriTrack.service.JwtService;
import com.nutritrack.NutriTrack.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link UsuarioController}.
 * Valida as rotas de listagem, busca, atualização e exclusão de usuários,
 * incluindo as regras de autorização.
 */
@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean(name = "authorization")
    private AuthorizationConfig authorization;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    /**
     * Testa a atualização do próprio perfil de usuário.
     * Deve retornar o status 200 (OK) quando o usuário autenticado atualiza seus próprios dados.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenUpdateOwnProfile_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO(
                "New Name",
                new BigDecimal("1.80"),
                new BigDecimal("75.0"),
                NivelAtividade.MODERADO,
                ObjetivoUsuario.MANTER_PESO
        );

        UserResponseDTO responseDTO = new UserResponseDTO(
                userId,
                "New Name",
                "user@example.com",
                new BigDecimal("1.80"),
                new BigDecimal("75.0"),
                LocalDate.now().minusYears(20),
                NivelAtividade.MODERADO,
                ObjetivoUsuario.MANTER_PESO,
                Role.ROLE_USER,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        when(authorization.isAuthorized(eq(userId), any())).thenReturn(true);
        when(usuarioService.updateProfile(eq(userId), any(UserProfileUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/usuarios/{id}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

    /**
     * Testa a tentativa de atualização do perfil de outro usuário.
     * Deve retornar o status 403 (Forbidden) quando um usuário tenta atualizar o perfil de outro.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenUpdateAnotherUsersProfile_shouldReturnForbidden() throws Exception {
        UUID anotherUserId = UUID.randomUUID();
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO(
                "New Name",
                new BigDecimal("1.80"),
                new BigDecimal("75.0"),
                NivelAtividade.MODERADO,
                ObjetivoUsuario.MANTER_PESO
        );

        when(authorization.isAuthorized(eq(anotherUserId), any())).thenReturn(false);

        mockMvc.perform(patch("/api/v1/usuarios/{id}", anotherUserId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden());
    }

    /**
     * Testa a exclusão do próprio perfil de usuário.
     * Deve retornar o status 204 (No Content) quando o usuário autenticado deleta seu próprio perfil.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenDeleteOwnProfile_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();

        when(authorization.isAuthorized(eq(userId), any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    /**
     * Testa a listagem de todos os usuários por um administrador.
     * Deve retornar o status 200 (OK) quando um usuário com a role ADMIN acessa a rota.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ROLE_ADMIN"})
    public void whenGetAllUsersAsAdmin_shouldReturnOk() throws Exception {
        when(usuarioService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }

    /**
     * Testa a busca do próprio perfil de usuário.
     * Deve retornar o status 200 (OK) quando o usuário autenticado busca seu próprio perfil.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenGetCurrentUserProfile_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponseDTO responseDTO = new UserResponseDTO(
                userId,
                "User Name",
                "user@example.com",
                new BigDecimal("1.80"),
                new BigDecimal("75.0"),
                LocalDate.now().minusYears(20),
                NivelAtividade.MODERADO,
                ObjetivoUsuario.MANTER_PESO,
                Role.ROLE_USER,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        when(authorization.isAuthorized(eq(userId), any())).thenReturn(true);
        when(usuarioService.findById(userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/usuarios/{id}", userId))
                .andExpect(status().isOk());
    }

    /**
     * Testa a tentativa de busca do perfil de outro usuário.
     * Deve retornar o status 403 (Forbidden) quando um usuário tenta buscar o perfil de outro.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenGetAnotherUserProfile_shouldReturnForbidden() throws Exception {
        UUID anotherUserId = UUID.randomUUID();

        when(authorization.isAuthorized(eq(anotherUserId), any())).thenReturn(false);

        mockMvc.perform(get("/api/v1/usuarios/{id}", anotherUserId))
                .andExpect(status().isForbidden());
    }
}
