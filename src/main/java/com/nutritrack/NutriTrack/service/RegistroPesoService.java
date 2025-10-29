package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.NutriTrack.dto.RegistroPesoResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroPeso;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.AccessDeniedException;
import com.nutritrack.NutriTrack.exception.ConflictException;
import com.nutritrack.NutriTrack.exception.ResourceNotFoundException;
import com.nutritrack.NutriTrack.mapper.RegistroPesoMapper;
import com.nutritrack.NutriTrack.repository.RegistroPesoRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações de CRUD e consultas de registros de peso de um usuário.
 *
 * Gerencia a criação, atualização, listagem por período e exclusão de registros de peso,
 * garantindo permissões de acesso, integridade dos dados e evitando duplicidade de registros na mesma data.
 */
@Service
@RequiredArgsConstructor
public class RegistroPesoService {

    private final UsuarioRepository usuarioRepository;
    private final RegistroPesoRepository registroPesoRepository;
    private final RegistroPesoMapper registroPesoMapper;

    /**
     * Cria um novo registro de peso para o usuário especificado.
     *
     * Verifica se já existe um registro para a mesma data e lança {@link ConflictException} se houver.
     *
     * @param usuarioId ID do usuário que está registrando o peso
     * @param requestDTO DTO contendo os dados do registro de peso
     * @return {@link RegistroPesoResponseDTO} com os dados do registro criado
     * @throws ResourceNotFoundException se o usuário não existir
     * @throws ConflictException se já existir um registro para a mesma data
     */
    @Transactional
    public RegistroPesoResponseDTO create(UUID usuarioId, RegistroPesoRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        Optional<RegistroPeso> existingRecord = registroPesoRepository.findByUsuario_IdAndDataMedicao(usuarioId, requestDTO.dataMedicao());

        if (existingRecord.isPresent()) {
            RegistroPeso registro = existingRecord.get();
            registro.setPesoKg(requestDTO.pesoKg());
            RegistroPeso updatedRegistro = registroPesoRepository.save(registro);
            return registroPesoMapper.toResponseDTO(updatedRegistro);
        } else {
            RegistroPeso registro = registroPesoMapper.toEntity(requestDTO);
            registro.setUsuario(usuario);
            RegistroPeso savedRegistro = registroPesoRepository.save(registro);
            return registroPesoMapper.toResponseDTO(savedRegistro);
        }
    }

    /**
     * Atualiza um registro de peso existente.
     *
     * Verifica se o usuário possui permissão para atualizar o registro e se não haverá conflito de datas.
     *
     * @param usuarioId ID do usuário dono do registro
     * @param registroId ID do registro a ser atualizado
     * @param requestDTO DTO contendo os novos dados do registro
     * @return {@link RegistroPesoResponseDTO} com os dados atualizados
     * @throws ResourceNotFoundException se o registro não existir
     * @throws AccessDeniedException se o usuário não for dono do registro
     * @throws ConflictException se já existir outro registro para a mesma data
     */
    @Transactional
    public RegistroPesoResponseDTO update(UUID usuarioId, UUID registroId, RegistroPesoRequestDTO requestDTO) {
        RegistroPeso registro = registroPesoRepository.findById(registroId)
            .orElseThrow(() -> new ResourceNotFoundException("Registro de peso não encontrado com o ID: " + registroId));

        if (!registro.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este registro.");
        }

        if (!registro.getDataMedicao().equals(requestDTO.dataMedicao())) {
            registroPesoRepository.findByUsuario_IdAndDataMedicao(usuarioId, requestDTO.dataMedicao())
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

    /**
     * Lista os registros de peso de um usuário dentro de um intervalo de datas.
     *
     * @param usuarioId ID do usuário
     * @param start Data inicial do intervalo
     * @param end Data final do intervalo
     * @return Lista de {@link RegistroPesoResponseDTO} contendo os registros encontrados
     */
    @Transactional(readOnly = true)
    public List<RegistroPesoResponseDTO> findByDateRange(UUID usuarioId, LocalDate start, LocalDate end) {
        return registroPesoRepository.findByUsuario_IdAndDataMedicaoBetween(usuarioId, start, end).stream()
            .map(registroPesoMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Exclui um registro de peso de um usuário.
     *
     * @param usuarioId ID do usuário dono do registro
     * @param registroId ID do registro a ser excluído
     * @throws ResourceNotFoundException se o registro não existir
     * @throws AccessDeniedException se o usuário não for dono do registro
     */
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
