package com.nutritrack.service;

import com.nutritrack.dto.UserRequestDTO;
import com.nutritrack.dto.UserResponseDTO;
import com.nutritrack.entity.Role;
import com.nutritrack.entity.Usuario;
import com.nutritrack.exception.ConflictException;
import com.nutritrack.mapper.UserMapper;
import com.nutritrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;

    public UserResponseDTO register(UserRequestDTO request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("Email já cadastrado");
        }
        Usuario usuario = userMapper.toEntity(request);
        usuario.setSenhaHash(request.senha());
        usuario.setRole(Role.ROLE_USER);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return userMapper.toResponseDTO(savedUsuario);
    }
}
