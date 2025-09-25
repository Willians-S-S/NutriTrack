package com.nutritrack.service;

import com.nutritrack.dto.AlimentoResponseDTO;
import com.nutritrack.entity.Alimento;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.AlimentoMapper;
import com.nutritrack.repository.AlimentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para {@link AlimentoService}.
 *
 * Usa Mockito para mockar dependências e testar comportamento dos métodos.
 */
@ExtendWith(MockitoExtension.class)
class AlimentoServiceTest {

    @Mock
    private AlimentoRepository alimentoRepository;

    @Mock
    private AlimentoMapper alimentoMapper;

    @InjectMocks
    private AlimentoService alimentoService;

    /**
     * Testa {@link AlimentoService#findById(UUID)} quando o alimento existe.
     * Deve retornar um {@link AlimentoResponseDTO} com os dados corretos.
     */
    @Test
    void findById_whenAlimentoExists_shouldReturnAlimentoResponseDTO() {
        UUID alimentoId = UUID.randomUUID();
        Alimento alimento = new Alimento(); // Configure com dados de teste
        alimento.setId(alimentoId);
        AlimentoResponseDTO expectedDto = new AlimentoResponseDTO(alimentoId, "Maçã", null, null, null, null, null);

        when(alimentoRepository.findById(alimentoId)).thenReturn(Optional.of(alimento));
        when(alimentoMapper.toResponseDTO(alimento)).thenReturn(expectedDto);

        AlimentoResponseDTO result = alimentoService.findById(alimentoId);

        assertNotNull(result);
        assertEquals(expectedDto.id(), result.id());
        verify(alimentoRepository).findById(alimentoId);
        verify(alimentoMapper).toResponseDTO(alimento);
    }

    /**
     * Testa {@link AlimentoService#findById(UUID)} quando o alimento não existe.
