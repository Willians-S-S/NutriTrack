package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.AlimentoRequestDTO;
import com.nutritrack.NutriTrack.service.AlimentoService;
import com.nutritrack.NutriTrack.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link AlimentoController}.
 * Valida as rotas de listagem, busca, criação, atualização e exclusão de alimentos.
 */
@WebMvcTest(AlimentoController.class)
@WithMockUser
class AlimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlimentoService alimentoService;

    @MockBean
    private JwtService jwtService;

    private final UUID alimentoId = UUID.randomUUID();

    /**
     * Testa a listagem de todos os alimentos.
     * Deve retornar o status 200 (OK).
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetAllAlimentos_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/alimentos"))
                .andExpect(status().isOk());
    }

    /**
     * Testa a busca de um alimento por ID.
     * Deve retornar o status 200 (OK) ao buscar com um ID válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetAlimentoById_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/alimentos/" + alimentoId))
                .andExpect(status().isOk());
    }

    /**
     * Testa a criação de um novo alimento com um usuário ADMIN.
     * Deve retornar o status 201 (Created) ao enviar dados válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void whenCreateAlimento_thenReturns201() throws Exception {
        AlimentoRequestDTO alimentoRequest = new AlimentoRequestDTO(
                "Frango Grelhado",
                new BigDecimal("165"),
                new BigDecimal("31"),
                new BigDecimal("0"),
                new BigDecimal("3.6")
        );

        mockMvc.perform(post("/api/v1/alimentos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alimentoRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa a atualização de um alimento existente com um usuário ADMIN.
     * Deve retornar o status 200 (OK) ao enviar dados válidos para atualização.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void whenUpdateAlimento_thenReturns200() throws Exception {
        AlimentoRequestDTO alimentoRequest = new AlimentoRequestDTO(
                "Frango Grelhado (Atualizado)",
                new BigDecimal("170"),
                new BigDecimal("32"),
                new BigDecimal("0"),
                new BigDecimal("4.0")
        );

        mockMvc.perform(put("/api/v1/alimentos/" + alimentoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alimentoRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Testa a exclusão de um alimento com um usuário ADMIN.
     * Deve retornar o status 204 (No Content) após a exclusão.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void whenDeleteAlimento_thenReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/alimentos/" + alimentoId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
