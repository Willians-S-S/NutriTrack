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

@Service
@RequiredArgsConstructor
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;
    private final AlimentoMapper alimentoMapper;

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

    @Transactional(readOnly = true)
    public AlimentoResponseDTO findById(UUID id) {
        return alimentoRepository.findById(id)
            .map(alimentoMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com o ID: " + id));
    }

    @Transactional
    public AlimentoResponseDTO create(AlimentoRequestDTO requestDTO) {
        Alimento alimento = alimentoMapper.toEntity(requestDTO);
        Alimento savedAlimento = alimentoRepository.save(alimento);
        return alimentoMapper.toResponseDTO(savedAlimento);
    }

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

    @Transactional
    public void delete(UUID id) {
        if (!alimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alimento não encontrado com o ID: " + id);
        }
        alimentoRepository.deleteById(id);
    }
}
