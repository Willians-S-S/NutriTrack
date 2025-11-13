package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.NutriTrack.service.JwtService;
import com.nutritrack.NutriTrack.service.RegistroPesoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link RegistroPesoController}.
 * Valida as rotas de criação, listagem, atualização e exclusão de registros de peso.
 */
@WebMvcTest(RegistroPesoController.class)
@WithMockUser
class RegistroPesoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistroPesoService registroPesoService;

    @MockBean
    private JwtService jwtService;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID registroId = UUID.randomUUID();

    /**
     * Testa a criação de um novo registro de peso.
     * Deve retornar o status 201 (Created) ao enviar dados válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenCreateRegistroPeso_thenReturns201() throws Exception {
        RegistroPesoRequestDTO request = new RegistroPesoRequestDTO(
                new BigDecimal("70.5"),
                LocalDate.now()
        );

        mockMvc.perform(post("/api/v1/registros-peso/" + usuarioId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa a listagem de registros de peso por intervalo de datas.
     * Deve retornar o status 200 (OK) ao buscar com um intervalo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetRegistrosByDateRange_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/registros-peso/usuario/" + usuarioId)
                        .param("start", LocalDate.now().toString())
                        .param("end", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    /**
     * Testa a atualização de um registro de peso existente.
     * Deve retornar o status 200 (OK) ao enviar dados válidos para atualização.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenUpdateRegistroPeso_thenReturns200() throws Exception {
        RegistroPesoRequestDTO request = new RegistroPesoRequestDTO(
                new BigDecimal("71.0"),
                LocalDate.now()
        );

        mockMvc.perform(put("/api/v1/registros-peso/" + usuarioId + "/" + registroId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    /**
     * Testa a exclusão de um registro de peso.
     * Deve retornar o status 204 (No Content) após a exclusão.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenDeleteRegistroPeso_thenReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/registros-peso/" + usuarioId + "/" + registroId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
