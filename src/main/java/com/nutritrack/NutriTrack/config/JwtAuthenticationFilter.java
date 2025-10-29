package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticação JWT customizado que se estende de {@link OncePerRequestFilter}.
 * <p>
 * Este filtro é responsável por interceptar todas as requisições HTTP, extrair o token JWT
 * do cabeçalho "Authorization", validar sua integridade e, se válido, carregar os detalhes
 * do usuário e configurar o objeto de autenticação no {@link SecurityContextHolder} do Spring Security.
 * <p>
 * Garante que o processo de autenticação seja executado uma única vez por requisição.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Lógica principal de filtragem que é executada para cada requisição.
     * <p>
     * 1. Verifica se o cabeçalho "Authorization" está presente e começa com "Bearer ".
     * 2. Extrai o token JWT.
     * 3. Extrai o email do usuário (username) do token.
     * 4. Se o email for válido e não houver autenticação prévia no contexto de segurança:
     * a. Carrega os detalhes do usuário através do {@link UserDetailsService}.
     * b. Valida o token com base nos detalhes do usuário.
     * c. Se o token for válido, cria um {@link UsernamePasswordAuthenticationToken} e o
     * define no {@link SecurityContextHolder}, autenticando o usuário para a requisição atual.
     * 5. Continua a cadeia de filtros.
     *
     * @param request O objeto {@link HttpServletRequest} da requisição.
     * @param response O objeto {@link HttpServletResponse} da resposta.
     * @param filterChain A cadeia de filtros para continuar o processamento da requisição.
     * @throws ServletException Se ocorrer um erro específico do servlet.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Request received for path: {}", request.getServletPath());
        if (request.getServletPath().contains("/v1/auth")) {
            log.info("Bypassing JWT filter for public auth endpoint");
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
