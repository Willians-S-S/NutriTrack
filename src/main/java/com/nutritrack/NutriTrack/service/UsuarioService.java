package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.UserProfileUpdateDTO;
import com.nutritrack.NutriTrack.dto.UserResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroPeso;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.ResourceNotFoundException;
import com.nutritrack.NutriTrack.mapper.UserMapper;
import com.nutritrack.NutriTrack.repository.RegistroPesoRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Serviço responsável por operações relacionadas ao gerenciamento de usuários.
 *
 * Inclui métodos para buscar, atualizar perfil, listar e excluir usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RegistroPesoRepository registroPesoRepository;
    private final UserMapper userMapper;

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário a ser buscado
     * @return {@link UserResponseDTO} com os dados do usuário encontrado
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        var ultimoRegistroPeso = registroPesoRepository.findFirstByUsuarioOrderByDataMedicaoDesc(usuario);
        var peso = ultimoRegistroPeso.map(RegistroPeso::getPesoKg).orElse(null);

        UserResponseDTO dto = userMapper.toResponseDTO(usuario);

        return new UserResponseDTO(
            dto.id(), 
            dto.nome(), 
            dto.email(), 
            dto.alturaM(), 
            peso, 
            dto.dataNascimento(), 
            dto.nivelAtividade(), 
            dto.objetivoUsuario(), 
            dto.role(), 
            dto.criadoEm(), 
            dto.atualizadoEm()
        );
    }

    /**
     * Atualiza o perfil de um usuário específico.
     *
     * Permite atualizar altura, nível de atividade e objetivo do usuário.
     *
     * @param userId ID do usuário a ser atualizado
     * @param updateDTO DTO contendo os novos dados do perfil
     * @return {@link UserResponseDTO} com os dados atualizados do usuário
     * @throws ResourceNotFoundException se o usuário não for encontrado
     */
    @Transactional
    public UserResponseDTO updateProfile(UUID userId, UserProfileUpdateDTO updateDTO) {
        System.out.println("updateDTO: " + updateDTO);
        Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

        if (updateDTO.nome() != null) {
            usuario.setNome(updateDTO.nome());
        }
        if (updateDTO.alturaM() != null) {
            usuario.setAlturaM(updateDTO.alturaM());
        }
        if (updateDTO.nivelAtividade() != null) {
            usuario.setNivelAtividade(updateDTO.nivelAtividade());
        }
        if (updateDTO.objetivoUsuario() != null) {
            usuario.setObjetivoUsuario(updateDTO.objetivoUsuario());
        }

        if (updateDTO.peso() != null) {
            LocalDate today = java.time.LocalDate.now();
            RegistroPeso registroPeso = registroPesoRepository.findByUsuario_IdAndDataMedicao(userId, today)
                .orElse(new RegistroPeso());

            registroPeso.setUsuario(usuario);
            registroPeso.setPesoKg(updateDTO.peso());
            registroPeso.setDataMedicao(today);
            
            if (registroPeso.getId() == null) {
                usuario.getRegistrosPeso().add(registroPeso);
            }
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);

        var ultimoRegistroPeso = registroPesoRepository.findFirstByUsuarioOrderByDataMedicaoDesc(updatedUsuario);
        var peso = ultimoRegistroPeso.map(RegistroPeso::getPesoKg).orElse(null);

        UserResponseDTO dto = userMapper.toResponseDTO(updatedUsuario);
        return new UserResponseDTO(
            dto.id(), 
            dto.nome(), 
            dto.email(), 
            dto.alturaM(), 
            peso, 
            dto.dataNascimento(), 
            dto.nivelAtividade(), 
            dto.objetivoUsuario(), 
            dto.role(), 
            dto.criadoEm(), 
            dto.atualizadoEm()
        );
    }

    /**
     * Lista todos os usuários com paginação.
     *
     * @param pageable objeto {@link Pageable} para controlar paginação e ordenação
     * @return Página de {@link UserResponseDTO} contendo os usuários encontrados
     */
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(userMapper::toResponseDTO);
    }

    /**
     * Exclui um usuário pelo seu ID.
     *
     * @param id ID do usuário a ser excluído
     * @throws ResourceNotFoundException se o usuário não existir
     */
    @Transactional
    public void delete(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
