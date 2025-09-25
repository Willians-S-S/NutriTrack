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

/**
 * Serviço responsável por operações de autenticação e registro de usuários.
 *
 * Utiliza {@link UsuarioRepository} para persistência e {@link UserMapper} para conversão
 * entre entidades e DTOs.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;

    /**
     * Registra um novo usuário no sistema.
     *
     * Verifica se o email já está cadastrado e, caso esteja, lança {@link ConflictException}.
     * Caso contrário, cria um novo usuário com role {@link Role#ROLE_USER} e salva no banco.
     *
     * @param request {@link UserRequestDTO} com os dados do usuário a ser registrado
     * @return {@link UserResponseDTO} com os dados do usuário registrado
     * @throws ConflictException se o email já estiver cadastrado
     */
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
