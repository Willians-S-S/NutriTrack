package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.AuthRequest;
import com.nutritrack.NutriTrack.dto.JwtResponse;
import com.nutritrack.NutriTrack.dto.UserRequestDTO;
import com.nutritrack.NutriTrack.dto.UserResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroPeso;
import com.nutritrack.NutriTrack.entity.Role;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.BusinessException;
import com.nutritrack.NutriTrack.exception.ConflictException;
import com.nutritrack.NutriTrack.mapper.UserMapper;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.ROLE_USER);

        RegistroPeso registroPeso = new RegistroPeso();
        registroPeso.setPesoKg(request.peso());
        registroPeso.setDataMedicao(LocalDate.now());
        registroPeso.setUsuario(usuario);

        if (usuario.getRegistrosPeso() == null) {
            usuario.setRegistrosPeso(new ArrayList<>());
        }
        usuario.getRegistrosPeso().add(registroPeso);


        Usuario savedUsuario = usuarioRepository.save(usuario);

        return new UserResponseDTO(
                savedUsuario.getId(), savedUsuario.getNome(),
                savedUsuario.getEmail(), savedUsuario.getAlturaM(),
                savedUsuario.getRegistrosPeso().getFirst().getPesoKg(),
                savedUsuario.getDataNascimento(),
                savedUsuario.getNivelAtividade(),
                savedUsuario.getObjetivoUsuario(),
                savedUsuario.getRole(),
                savedUsuario.getCriadoEm(),
                savedUsuario.getAtualizadoEm());

//        return userMapper.toResponseDTO(savedUsuario);
    }

    /**
     * Autentica um usuário usando as credenciais fornecidas e gera um Token Web JSON (JWT).
     * <p>
     * O método segue os seguintes passos:
     * 1. Tenta autenticar o usuário através do {@code AuthenticationManager} com o email e senha.
     * 2. Se a autenticação falhar (ex.: senha incorreta), uma {@link BadCredentialsException} é capturada
     * e convertida em uma {@link BusinessException} customizada.
     * 3. Se a autenticação for bem-sucedida, busca o objeto {@code Usuario} completo no banco de dados.
     * 4. Gera um JWT usando o {@code JwtService} com base nos detalhes do usuário.
     *
     * @param request Objeto {@code AuthRequest} contendo o email (username) e a senha do usuário.
     * @return Um objeto {@code JwtResponse} contendo o JWT gerado.
     * @throws BusinessException Se o email ou a senha forem inválidos, ou se o usuário não for encontrado
     * após a autenticação (o que é improvável, mas garante robustez).
     */
    public JwtResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException("Email ou senha inválidos");
        }
        var user = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));
        String jwtToken = jwtService.generateToken(user);
        return new JwtResponse(jwtToken);
    }
}
