package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.config.AuthorizationConfig;
import com.nutritrack.NutriTrack.config.JwtAuthenticationFilter;
import com.nutritrack.NutriTrack.config.SecurityConfig;
import com.nutritrack.NutriTrack.dto.UserProfileUpdateDTO;
import com.nutritrack.NutriTrack.dto.UserResponseDTO;
import com.nutritrack.NutriTrack.entity.NivelAtividade;
import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import com.nutritrack.NutriTrack.entity.Role;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import({SecurityConfig.class, AuthorizationConfig.class})
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AuthorizationConfig authorizationConfig;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private UsuarioRepository usuarioRepository;



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

        when(authorizationConfig.isAuthorized(eq(userId), any())).thenReturn(true);
        when(usuarioService.updateProfile(eq(userId), any(UserProfileUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/usuarios/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }

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

        when(authorizationConfig.isAuthorized(eq(anotherUserId), any())).thenReturn(false);

        mockMvc.perform(patch("/api/v1/usuarios/{id}", anotherUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@example.com", authorities = {"ROLE_USER"})
    public void whenDeleteOwnProfile_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();

        when(authorizationConfig.isAuthorized(eq(userId), any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = {"ROLE_ADMIN"})
    public void whenGetAllUsersAsAdmin_shouldReturnOk() throws Exception {
        when(usuarioService.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }

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

        when(usuarioService.findById(userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/usuarios/{id}", userId))
                .andExpect(status().isOk());
    }
}
