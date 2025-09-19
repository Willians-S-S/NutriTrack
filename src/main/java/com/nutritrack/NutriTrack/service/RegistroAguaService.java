package com.nutritrack.service;

import com.nutritrack.dto.RegistroAguaDiarioDTO;
import com.nutritrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.entity.RegistroAgua;
import com.nutritrack.entity.Usuario;
import com.nutritrack.exception.AccessDeniedException;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.RegistroAguaMapper;
import com.nutritrack.repository.RegistroAguaRepository;
import com.nutritrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroAguaService {

    private final RegistroAguaRepository registroAguaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroAguaMapper registroAguaMapper;

    @Transactional
    public RegistroAguaResponseDTO create(UUID usuarioId, RegistroAguaRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        RegistroAgua registro = registroAguaMapper.toEntity(requestDTO);
        registro.setUsuario(usuario);

        RegistroAgua savedRegistro = registroAguaRepository.save(registro);
        return registroAguaMapper.toResponseDTO(savedRegistro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAguaResponseDTO> findByDateRange(UUID usuarioId, LocalDate start, LocalDate end) {
        return registroAguaRepository.findByUsuarioIdAndDataMedicaoBetween(usuarioId, start, end).stream()
            .map(registroAguaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroAguaDiarioDTO> getDailySummary(UUID usuarioId, LocalDate start, LocalDate end) {
        List<RegistroAgua> registros = registroAguaRepository.findByUsuarioIdAndDataMedicaoBetween(usuarioId, start, end);

        Map<LocalDate, Integer> dailyTotals = registros.stream()
            .collect(Collectors.groupingBy(
                RegistroAgua::getDataMedicao,
                Collectors.summingInt(RegistroAgua::getQuantidadeMl)
            ));

        return dailyTotals.entrySet().stream()
            .map(entry -> new RegistroAguaDiarioDTO(entry.getKey(), entry.getValue()))
            .sorted((a, b) -> b.data().compareTo(a.data())) // Ordena do mais recente para o mais antigo
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(UUID usuarioId, UUID registroId) {
        RegistroAgua registro = registroAguaRepository.findById(registroId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de água não encontrado com o ID: " + registroId));
        if (!registro.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para excluir este registro.");
        }
        registroAguaRepository.delete(registro);
    }
}
