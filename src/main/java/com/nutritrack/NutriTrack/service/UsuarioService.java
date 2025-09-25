package com.nutritrack.service;

import com.nutritrack.dto.UserProfileUpdateDTO;
import com.nutritrack.dto.UserResponseDTO;
import com.nutritrack.entity.Usuario;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.UserMapper;
import com.nutritrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return usuarioRepository.findById(id)
            .map(userMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
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
        Usuario usuario = usuarioRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

        usuario.setAlturaM(updateDTO.alturaM());
        usuario.setNivelAtividade(updateDTO.nivelAtividade());
        usuario.setObjetivoUsuario(updateDTO.objetivoUsuario());

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return userMapper.toResponseDTO(updatedUsuario);
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
