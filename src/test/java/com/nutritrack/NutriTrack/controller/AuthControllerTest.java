package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.AuthRequest;
import com.nutritrack.NutriTrack.dto.UserRequestDTO;
import com.nutritrack.NutriTrack.entity.NivelAtividade;
import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import com.nutritrack.NutriTrack.service.AuthService;
import com.nutritrack.NutriTrack.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link AuthController}.
 * Foca em validar as rotas de registro e login.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    /**
     * Testa o registro de um novo usuário.
     * Deve retornar o status 201 (Created) ao enviar dados de usuário válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenRegisterUser_thenReturns201() throws Exception {
        UserRequestDTO userRequest = new UserRequestDTO(
                "Test User",
                "test@example.com",
                "password123",
                new BigDecimal("1.75"),
                LocalDate.of(1990, 1, 1),
                NivelAtividade.MODERADO,
                ObjetivoUsuario.MANTER_PESO,
                new BigDecimal("70.5")
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa o processo de login de um usuário.
     * Deve retornar o status 200 (OK) ao enviar credenciais válidas.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenLoginUser_thenReturns200() throws Exception {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }
}
