package com.nutritrack.NutriTrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuração principal de segurança para a aplicação Spring Boot.
 * <p>
 * Esta classe utiliza o Spring Security para configurar:
 * <ul>
 * <li>A desativação do CSRF.</li>
 * <li>A política de criação de sessão como {@link SessionCreationPolicy#STATELESS},
 * indicando que a aplicação não manterá estado de sessão entre as requisições.</li>
 * <li>A autorização de endpoints, permitindo acesso público a rotas de autenticação e documentação
 * (Swagger/OpenAPI), bem como a visualização de alimentos (GET), e exigindo autenticação
 * para todos os outros endpoints.</li>
 * <li>A injeção do filtro JWT ({@code JwtAuthenticationFilter}) antes do filtro
 * padrão de autenticação de senha de usuário.</li>
 * <li>O provedor de autenticação customizado ({@code AuthenticationProvider}).</li>
 * </ul>
 * <p>
 * O uso de {@code @EnableMethodSecurity(securedEnabled = true)} permite a segurança
 * em nível de método usando a anotação {@code @Secured}.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/auth/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    /**
     * Define a cadeia de filtros de segurança para as requisições HTTP.
     *
     * @param http o objeto {@link HttpSecurity} que permite configurar a segurança web.
     * @return a {@link SecurityFilterChain} configurada.
     * @throws Exception se houver um erro durante a construção da cadeia de filtros.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("--- DEBUG DE SECURITY FILTER CHAIN ---");
        http.cors().and()
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/alimentos/**").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/usuarios/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
