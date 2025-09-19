package com.nutritrack.service;

import com.nutritrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.dto.RegistroPesoResponseDTO;
import com.nutritrack.entity.RegistroPeso;
import com.nutritrack.entity.Usuario;
import com.nutritrack.exception.AccessDeniedException;
import com.nutritrack.exception.ConflictException;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.RegistroPesoMapper;
import com.nutritrack.repository.RegistroPesoRepository;
import com.nutritrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroPesoService {

    private final RegistroPesoRepository registroPesoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroPesoMapper registroPesoMapper;

    @Transactional
    public RegistroPesoResponseDTO create(UUID usuarioId, RegistroPesoRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        registroPesoRepository.findByUsuarioIdAndDataMedicao(usuarioId, requestDTO.dataMedicao())
            .ifPresent(r -> {
                throw new ConflictException("Já existe um registro de peso para a data: " + requestDTO.dataMedicao());
            });

        RegistroPeso registro = registroPesoMapper.toEntity(requestDTO);
        registro.setUsuario(usuario);

        RegistroPeso savedRegistro = registroPesoRepository.save(registro);
        return registroPesoMapper.toResponseDTO(savedRegistro);
    }

    @Transactional
    public RegistroPesoResponseDTO update(UUID usuarioId, UUID registroId, RegistroPesoRequestDTO requestDTO) {
        RegistroPeso registro = registroPesoRepository.findById(registroId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de peso não encontrado com o ID: " + registroId));

        if (!registro.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este registro.");
        }

        if (!registro.getDataMedicao().equals(requestDTO.dataMedicao())) {
            registroPesoRepository.findByUsuarioIdAndDataMedicao(usuarioId, requestDTO.dataMedicao())
                .ifPresent(r -> {
                    if (!r.getId().equals(registroId)) {
                        throw new ConflictException("Já existe um registro de peso para a data: " + requestDTO.dataMedicao());
                    }
                });
        }

        registro.setPesoKg(requestDTO.pesoKg());
        registro.setDataMedicao(requestDTO.dataMedicao());

        RegistroPeso updatedRegistro = registroPesoRepository.save(registro);
        return registroPesoMapper.toResponseDTO(updatedRegistro);
    }

    @Transactional(readOnly = true)
    public List<RegistroPesoResponseDTO> findByDateRange(UUID usuarioId, LocalDate start, LocalDate end) {
        return registroPesoRepository.findByUsuarioIdAndDataMedicaoBetween(usuarioId, start, end).stream()
            .map(registroPesoMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UUID usuarioId, UUID registroId) {
        RegistroPeso registro = registroPesoRepository.findById(registroId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de peso não encontrado com o ID: " + registroId));
        if (!registro.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para excluir este registro.");
        }
        registroPesoRepository.delete(registro);
    }
}
