package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.ResourceNotFoundException;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import com.nutritrack.NutriTrack.service.JwtService;
import io.jsonwebtoken.Jwt;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * Serviço que fornece lógicas de autorização customizadas para serem utilizadas
 * com as anotações de segurança do Spring (e.g., {@code @PreAuthorize}).
 * <p>
 * Este bean é nomeado como "authorization" para ser facilmente referenciado
 * em expressões SpEL (Spring Expression Language), permitindo verificações de
 * permissão dinâmicas e baseadas no contexto da requisição.
 */
@RequiredArgsConstructor
@Component("authorization")
public class AuthorizationConfig {

    private final UsuarioRepository clientRepository;
    private final JwtService jwtService;


    /**
     * Verifica se o usuário autenticado é o "dono" do recurso (cliente) que está tentando acessar.
     * <p>
     * Esta verificação é um pilar da autorização em nível de recurso, garantindo que um
     * usuário só possa visualizar ou modificar seus próprios dados. A lógica compara o ID do
     * usuário (extraído do claim 'sub' do token JWT) com o ID do recurso que está sendo solicitado.
     *
     * @param id             O ID do recurso (neste caso, o ID do cliente) a ser verificado.
     * @param authentication O objeto de autenticação do Spring Security, contendo os detalhes
     * do usuário logado (incluindo o JWT).
     * @return {@code true} se o ID do usuário no token for igual ao ID do recurso,
     * {@code false} caso contrário.
     */
    public boolean isAuthorized(UUID id, Authentication authentication){
        Usuario usuario = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("O usuario informado não foi encontrado."));

        Usuario user = (Usuario) authentication.getPrincipal();

        return user.getId().equals(id);
    }
}