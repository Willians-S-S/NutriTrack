package com.nutritrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.dto.AlimentoRequestDTO;
import com.nutritrack.service.AlimentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link com.nutritrack.controller.AlimentoController}.
 *
 * Usa MockMvc para simular requisições HTTP e valida os status de resposta
 * para diferentes endpoints e diferentes roles de usuário.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AlimentoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlimentoService alimentoService; // Mock do serviço para isolar testes do controller

    /**
     * Testa o endpoint GET /api/v1/alimentos.
     * Deve retornar HTTP 200 OK.
     */
    @Test
    void getAllAlimentos_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/alimentos"))
            .andExpect(status().isOk());
    }

    /**
     * Testa o endpoint POST /api/v1/alimentos com um usuário ADMIN.
     * Deve permitir a criação do alimento e retornar HTTP 201 Created.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void createAlimento_withAdminRole_shouldReturnCreated() throws Exception {
        AlimentoRequestDTO requestDTO = new AlimentoRequestDTO(
            "Pera",
            new BigDecimal("50.0"),
            new BigDecimal("0.4"),
            new BigDecimal("15.0"),
            new BigDecimal("0.1")
        );

        mockMvc.perform(post("/api/v1/alimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isCreated());
    }

    /**
     * Testa o endpoint POST /api/v1/alimentos com um usuário USER.
     * Usuário sem permissão para criar alimento deve receber HTTP 403 Forbidden.
     */
    @Test
    @WithMockUser(roles = "USER")
    void createAlimento_withUserRole_shouldReturnForbidden() throws Exception {
        AlimentoRequestDTO requestDTO = new AlimentoRequestDTO(
            "Uva",
            new BigDecimal("60.0"),
            new BigDecimal("0.5"),
            new BigDecimal("18.0"),
            new BigDecimal("0.2")
        );

        mockMvc.perform(post("/api/v1/alimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isForbidden());
    }
}
