package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.NutriTrack.service.JwtService;
import com.nutritrack.NutriTrack.service.RegistroAguaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link RegistroAguaController}.
 * Valida as rotas de criação, listagem, resumo e exclusão de registros de água.
 */
@WebMvcTest(RegistroAguaController.class)
@WithMockUser
class RegistroAguaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistroAguaService registroAguaService;

    @MockBean
    private JwtService jwtService;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID registroId = UUID.randomUUID();

    /**
     * Testa a criação de um novo registro de consumo de água.
     * Deve retornar o status 201 (Created) ao enviar dados válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenCreateRegistroAgua_thenReturns201() throws Exception {
        RegistroAguaRequestDTO request = new RegistroAguaRequestDTO(
                250,
                LocalDate.now()
        );

        mockMvc.perform(post("/api/v1/registros-agua/" + usuarioId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa a listagem de registros de água por intervalo de datas.
     * Deve retornar o status 200 (OK) ao buscar com um intervalo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetRegistrosByDateRange_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/registros-agua/usuario/" + usuarioId)
                        .param("start", LocalDate.now().toString())
                        .param("end", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    /**
     * Testa o resumo diário de consumo de água.
     * Deve retornar o status 200 (OK) ao solicitar o resumo com um intervalo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetDailySummary_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/registros-agua/summary/" + usuarioId)
                        .param("start", LocalDate.now().toString())
                        .param("end", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    /**
     * Testa a exclusão de um registro de água.
     * Deve retornar o status 204 (No Content) após a exclusão.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenDeleteRegistroAgua_thenReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/registros-agua/" + usuarioId + "/" + registroId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
