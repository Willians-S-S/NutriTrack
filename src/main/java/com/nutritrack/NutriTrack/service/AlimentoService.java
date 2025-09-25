package com.nutritrack.service;

import com.nutritrack.dto.AlimentoRequestDTO;
import com.nutritrack.dto.AlimentoResponseDTO;
import com.nutritrack.entity.Alimento;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.AlimentoMapper;
import com.nutritrack.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de alimentos.
 * 
 * Permite realizar operações CRUD (criar, consultar, atualizar e excluir)
 * e buscar alimentos com filtro por nome e paginação.
 */
@Service
@RequiredArgsConstructor
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;
    private final AlimentoMapper alimentoMapper;

    /**
     * Busca todos os alimentos, com opção de filtro por nome e paginação.
     *
     * @param pageable Objeto de paginação
     * @param nome     Nome ou parte do nome do alimento para filtro (opcional)
     * @return Página de {@link AlimentoResponseDTO} com os alimentos encontrados
     */
    @Transactional(readOnly = true)
    public Page<AlimentoResponseDTO> findAll(Pageable pageable, String nome) {
        Page<Alimento> page;
        if (nome != null && !nome.isEmpty()) {
            page = alimentoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = alimentoRepository.findAll(pageable);
        }
        return page.map(alimentoMapper::toResponseDTO);
    }

    /**
     * Busca um alimento pelo seu ID.
     *
     * @param id ID do alimento
     * @return {@link AlimentoResponseDTO} do alimento encontrado
     * @throws ResourceNotFoundException se nenhum alimento for encontrado com o ID informado
     */
    @Transactional(readOnly = true)
    public AlimentoResponseDTO findById(UUID id) {
        return alimentoRepository.findById(id)
            .map(alimentoMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com o ID: " + id));
    }

    /**
     * Cria um novo alimento no sistema.
     *
     * @param requestDTO DTO contendo os dados do alimento
     * @return {@link AlimentoResponseDTO} com o alimento criado
     */
    @Transactional
    public AlimentoResponseDTO create(AlimentoRequestDTO requestDTO) {
        Alimento alimento = alimentoMapper.toEntity(requestDTO);
        Alimento savedAlimento = alimentoRepository.save(alimento);
        return alimentoMapper.toResponseDTO(savedAlimento);
    }

    /**
     * Atualiza um alimento existente.
     *
     * @param id         ID do alimento a ser atualizado
     * @param requestDTO DTO contendo os novos dados do alimento
     * @return {@link AlimentoResponseDTO} com o alimento atualizado
     * @throws ResourceNotFoundException se nenhum alimento for encontrado com o ID informado
     */
    @Transactional
    public AlimentoResponseDTO update(UUID id, AlimentoRequestDTO requestDTO) {
        Alimento alimento = alimentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com o ID: " + id));

        alimento.setNome(requestDTO.nome());
        alimento.setCalorias(requestDTO.calorias());
        alimento.setProteinasG(requestDTO.proteinasG());
        alimento.setCarboidratosG(requestDTO.carboidratosG());
        alimento.setGordurasG(requestDTO.gordurasG());

        Alimento updatedAlimento = alimentoRepository.save(alimento);
        return alimentoMapper.toResponseDTO(updatedAlimento);
    }

    /**
     * Remove um alimento pelo seu ID.
     *
     * @param id ID do alimento a ser excluído
     * @throws ResourceNotFoundException se nenhum alimento for encontrado com o ID informado
     */
    @Transactional
    public void delete(UUID id) {
        if (!alimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alimento não encontrado com o ID: " + id);
        }
        alimentoRepository.deleteById(id);
    }
}
