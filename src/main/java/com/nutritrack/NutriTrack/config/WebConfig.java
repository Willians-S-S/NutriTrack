package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

/**
 * Configurações centrais de segurança e CORS da aplicação.
 * <p>
 * Esta classe:
 * <ul>
 *   <li>Define a política de CORS (origens, métodos, headers, cache de preflight);</li>
 *   <li>Habilita o CORS e desabilita CSRF no {@link SecurityFilterChain};</li>
 *   <li>Libera o preflight HTTP {@code OPTIONS} e rotas públicas (auth e Swagger);</li>
 *   <li>Fornece beans de autenticação (UserDetailsService, AuthenticationProvider, AuthenticationManager) e
 *       hash de senha ({@link PasswordEncoder}).</li>
 * </ul>
 * Recomenda-se manter a lista de origens liberadas sincronizada com os ambientes (dev/produção).
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    /**
     * Repositório usado para carregar usuários por e-mail dentro do {@link UserDetailsService}.
     */
    private final UsuarioRepository usuarioRepository;

    // ====== CORS ======

    /**
     * Define a configuração de CORS global da aplicação.
     * <p>
     * Usa {@code setAllowedOriginPatterns} para permitir credenciais com padrões de origem
     * (evita o uso inválido de {@code *} quando {@code allowCredentials = true}).
     *
     * @return fonte de configuração de CORS aplicada a todos os endpoints ({@code /**}).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Se for usar cookies/credentials (Authorization), prefira AllowedOriginPatterns.
        cfg.setAllowedOriginPatterns(List.of(
                "https://nutritrack-production-cfa6.up.railway.app", // produção
                "http://localhost:*",                                // dev vite/webpack
                "http://127.0.0.1:*"
        ));
        cfg.setAllowCredentials(true);
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        // Pode-se listar explicitamente (ex.: "Authorization","Content-Type"), aqui liberamos todos os headers.
        cfg.setAllowedHeaders(List.of("*"));
        // Cabeçalhos expostos ao cliente (ex.: token em Location/Authorization quando aplicável).
        cfg.setExposedHeaders(List.of("Location", "Authorization"));
        // Cache do preflight no navegador.
        cfg.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // ====== Security (habilita CORS e libera preflight/rotas públicas) ======

    /**
     * Cadeia principal de filtros de segurança do Spring Security.
     * <p>
     * - Desabilita CSRF (APIs stateless);<br>
     * - Habilita CORS usando o {@link #corsConfigurationSource()};<br>
     * - Libera requisições {@code OPTIONS} (preflight) e rotas públicas (auth e Swagger);<br>
     * - Exige autenticação nas demais rotas.
     *
     * @param http builder de configuração de segurança.
     * @return {@link SecurityFilterChain} configurada.
     * @throws Exception em caso de erro de construção da cadeia de filtros.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Rotas públicas
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Demais rotas precisam de autenticação
                        .anyRequest().authenticated()
                )
                .build();
    }

    // ====== Auth beans ======

    /**
     * Serviço de carregamento de detalhes do usuário por e-mail (username).
     *
     * @return implementação de {@link UserDetailsService} baseada em {@link UsuarioRepository}.
     * @throws UsernameNotFoundException quando o usuário não é encontrado.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
    }

    /**
     * Provedor de autenticação baseado em {@link DaoAuthenticationProvider},
     * configurado com o {@link UserDetailsService} e o {@link PasswordEncoder}.
     *
     * @return bean {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Expõe o {@link AuthenticationManager} obtido a partir da {@link AuthenticationConfiguration}.
     *
     * @param config configuração de autenticação do Spring.
     * @return {@link AuthenticationManager} para uso em fluxos de login.
     * @throws Exception se não for possível obter o manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Codificador de senhas padrão (BCrypt).
     *
     * @return {@link PasswordEncoder} com {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
