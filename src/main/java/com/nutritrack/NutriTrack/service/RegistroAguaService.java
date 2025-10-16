package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.RegistroAguaDiarioDTO;
import com.nutritrack.NutriTrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.NutriTrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroAgua;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.AccessDeniedException;
import com.nutritrack.NutriTrack.exception.ResourceNotFoundException;
import com.nutritrack.NutriTrack.mapper.RegistroAguaMapper;
import com.nutritrack.NutriTrack.repository.RegistroAguaRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações de CRUD e consultas de registros de água de um usuário.
 *
 * Gerencia a criação, listagem por período, resumo diário e exclusão de registros de consumo de água,
 * garantindo permissões de acesso e integridade dos dados.
 */
@Service
@RequiredArgsConstructor
public class RegistroAguaService {

    private final RegistroAguaRepository registroAguaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroAguaMapper registroAguaMapper;

    /**
     * Cria um novo registro de água para o usuário especificado.
     *
     * @param usuarioId ID do usuário que está registrando a água
     * @param requestDTO DTO contendo os dados do registro de água
     * @return {@link RegistroAguaResponseDTO} com os dados do registro criado
     * @throws ResourceNotFoundException se o usuário não existir
     */
    @Transactional
    public RegistroAguaResponseDTO create(UUID usuarioId, RegistroAguaRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        RegistroAgua registro = registroAguaMapper.toEntity(requestDTO);
        registro.setUsuario(usuario);

        RegistroAgua savedRegistro = registroAguaRepository.save(registro);
        return registroAguaMapper.toResponseDTO(savedRegistro);
    }

    /**
     * Lista os registros de água de um usuário dentro de um intervalo de datas.
     *
     * @param usuarioId ID do usuário
     * @param start Data inicial do intervalo
     * @param end Data final do intervalo
     * @return Lista de {@link RegistroAguaResponseDTO} contendo os registros encontrados
     */
    @Transactional(readOnly = true)
    public List<RegistroAguaResponseDTO> findByDateRange(UUID usuarioId, LocalDate start, LocalDate end) {
        return registroAguaRepository.findByUsuarioIdAndDataMedicaoBetween(usuarioId, start, end).stream()
            .map(registroAguaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retorna um resumo diário do consumo de água de um usuário em um intervalo de datas.
     *
     * Soma a quantidade de água consumida por dia e ordena os resultados do mais recente para o mais antigo.
     *
     * @param usuarioId ID do usuário
     * @param start Data inicial do intervalo
     * @param end Data final do intervalo
     * @return Lista de {@link RegistroAguaDiarioDTO} contendo a data e a quantidade total de água consumida
     */
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

    /**
     * Exclui um registro de água de um usuário.
     *
     * @param usuarioId ID do usuário dono do registro
     * @param registroId ID do registro a ser excluído
     * @throws ResourceNotFoundException se o registro não existir
     * @throws AccessDeniedException se o usuário não for dono do registro
     */
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
