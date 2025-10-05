package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração que define beans essenciais para a segurança e autenticação
 * da aplicação, utilizando o Spring Security.
 * <p>
 * Responsável por prover:
 * <ul>
 * <li>O serviço de detalhes do usuário ({@link UserDetailsService}).</li>
 * <li>O provedor de autenticação ({@link AuthenticationProvider}).</li>
 * <li>O gerenciador de autenticação ({@link AuthenticationManager}).</li>
 * <li>O codificador de senhas ({@link PasswordEncoder}).</li>
 * </ul>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um bean para o serviço de detalhes do usuário.
     * <p>
     * Utiliza o {@code UsuarioRepository} para carregar um usuário a partir
     * do email (que atua como username).
     *
     * @return Uma instância de {@link UserDetailsService}.
     * @throws UsernameNotFoundException se o usuário não for encontrado pelo email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
    }

    /**
     * Cria um bean para o provedor de autenticação.
     * <p>
     * Este provedor utiliza o {@link DaoAuthenticationProvider} e configura-o
     * com o {@code UserDetailsService} e o {@code PasswordEncoder} definidos nesta classe,
     * permitindo que o Spring Security realize a autenticação.
     *
     * @return Uma instância de {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Cria um bean para o gerenciador de autenticação.
     * <p>
     * Obtém a instância do {@link AuthenticationManager} a partir da
     * {@link AuthenticationConfiguration} do Spring Security.
     *
     * @param config A configuração de autenticação fornecida pelo Spring.
     * @return Uma instância de {@link AuthenticationManager}.
     * @throws Exception se houver um erro ao obter o gerenciador de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Cria um bean para o codificador de senhas.
     * <p>
     * Utiliza o {@link BCryptPasswordEncoder}, que é um algoritmo robusto
     * e recomendado para hash de senhas.
     *
     * @return Uma instância de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
