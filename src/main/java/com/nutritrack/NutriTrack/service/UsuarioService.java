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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        return usuarioRepository.findById(id)
            .map(userMapper::toResponseDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }
    
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
}
