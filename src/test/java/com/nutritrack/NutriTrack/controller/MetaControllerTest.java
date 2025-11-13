package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.enums.TipoMeta;
import com.nutritrack.NutriTrack.service.JwtService;
import com.nutritrack.NutriTrack.service.MetaService;
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
 * Testes de integração para o {@link MetaController}.
 * Valida as rotas de criação, busca, atualização e progresso de metas.
 */
@WebMvcTest(MetaController.class)
@WithMockUser
class MetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MetaService metaService;

    @MockBean
    private JwtService jwtService;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID metaId = UUID.randomUUID();

    /**
     * Testa a criação de uma nova meta.
     * Deve retornar o status 201 (Created) ao enviar dados válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenCreateMeta_thenReturns201() throws Exception {
        MetaRequestDTO metaRequest = new MetaRequestDTO(
                TipoMeta.DIARIA,
                new BigDecimal("2000"),
                new BigDecimal("150"),
                new BigDecimal("250"),
                new BigDecimal("50"),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        mockMvc.perform(post("/api/v1/usuarios/" + usuarioId + "/metas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(metaRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa a busca de metas por tipo.
     * Deve retornar o status 200 (OK) ao buscar com um tipo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetMetas_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/" + usuarioId + "/metas")
                        .param("tipo", TipoMeta.DIARIA.toString()))
                .andExpect(status().isOk());
    }

    /**
     * Testa a atualização de uma meta existente.
     * Deve retornar o status 200 (OK) ao enviar dados válidos para atualização.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenUpdateMeta_thenReturns200() throws Exception {
        MetaRequestDTO metaRequest = new MetaRequestDTO(
                TipoMeta.DIARIA,
                new BigDecimal("2100"),
                new BigDecimal("160"),
                new BigDecimal("260"),
                new BigDecimal("55"),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        mockMvc.perform(put("/api/v1/usuarios/" + usuarioId + "/metas/" + metaId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(metaRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Testa a busca do progresso de uma meta.
     * Deve retornar o status 200 (OK) ao solicitar o progresso com um tipo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetProgresso_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/" + usuarioId + "/metas/progresso")
                        .param("tipo", TipoMeta.DIARIA.toString()))
                .andExpect(status().isOk());
    }
}
