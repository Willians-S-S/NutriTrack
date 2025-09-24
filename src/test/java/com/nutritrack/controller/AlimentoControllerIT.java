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

@SpringBootTest
@AutoConfigureMockMvc
class AlimentoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlimentoService alimentoService; // Mocking service layer to isolate controller tests

    @Test
    void getAllAlimentos_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/alimentos"))
            .andExpect(status().isOk());
    }

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
