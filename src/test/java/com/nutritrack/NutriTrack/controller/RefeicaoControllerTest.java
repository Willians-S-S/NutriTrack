package com.nutritrack.NutriTrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nutritrack.NutriTrack.dto.ItemRefeicaoRequestDTO;
import com.nutritrack.NutriTrack.dto.RefeicaoRequestDTO;
import com.nutritrack.NutriTrack.entity.TipoRefeicao;
import com.nutritrack.NutriTrack.entity.UnidadeMedida;
import com.nutritrack.NutriTrack.service.JwtService;
import com.nutritrack.NutriTrack.service.RefeicaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o {@link RefeicaoController}.
 * Valida as rotas de criação, busca, listagem, atualização e exclusão de refeições.
 */
@WebMvcTest(RefeicaoController.class)
@WithMockUser
class RefeicaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RefeicaoService refeicaoService;

    @MockBean
    private JwtService jwtService;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID refeicaoId = UUID.randomUUID();
    private final UUID alimentoId = UUID.randomUUID();

    /**
     * Testa a criação de uma nova refeição.
     * Deve retornar o status 201 (Created) ao enviar dados válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenCreateRefeicao_thenReturns201() throws Exception {
        ItemRefeicaoRequestDTO item = new ItemRefeicaoRequestDTO(
                alimentoId,
                new BigDecimal("150.0"),
                UnidadeMedida.GRAMA,
                "Com pouco sal"
        );

        RefeicaoRequestDTO refeicaoRequest = new RefeicaoRequestDTO(
                TipoRefeicao.ALMOCO,
                OffsetDateTime.now(),
                "Refeição balanceada",
                Collections.singletonList(item)
        );

        mockMvc.perform(post("/api/v1/refeicoes/" + usuarioId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refeicaoRequest)))
                .andExpect(status().isCreated());
    }

    /**
     * Testa a busca de uma refeição por ID.
     * Deve retornar o status 200 (OK) ao buscar com IDs válidos.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetRefeicaoById_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/refeicoes/" + usuarioId + "/" + refeicaoId))
                .andExpect(status().isOk());
    }

    /**
     * Testa a listagem de refeições por intervalo de datas.
     * Deve retornar o status 200 (OK) ao buscar com um intervalo válido.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenGetRefeicoesByDateRange_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/refeicoes/usuario/" + usuarioId)
                        .param("start", LocalDate.now().toString())
                        .param("end", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk());
    }

    /**
     * Testa a atualização de uma refeição existente.
     * Deve retornar o status 200 (OK) ao enviar dados válidos para atualização.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenUpdateRefeicao_thenReturns200() throws Exception {
        ItemRefeicaoRequestDTO item = new ItemRefeicaoRequestDTO(
                alimentoId,
                new BigDecimal("200.0"),
                UnidadeMedida.GRAMA,
                "Sem sal"
        );

        RefeicaoRequestDTO refeicaoRequest = new RefeicaoRequestDTO(
                TipoRefeicao.JANTAR,
                OffsetDateTime.now(),
                "Refeição leve",
                Collections.singletonList(item)
        );

        mockMvc.perform(put("/api/v1/refeicoes/" + usuarioId + "/" + refeicaoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refeicaoRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Testa a exclusão de uma refeição.
     * Deve retornar o status 204 (No Content) após a exclusão.
     * @throws Exception se ocorrer um erro durante a execução do MockMvc.
     */
    @Test
    void whenDeleteRefeicao_thenReturns204() throws Exception {
        mockMvc.perform(delete("/api/v1/refeicoes/" + usuarioId + "/" + refeicaoId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
