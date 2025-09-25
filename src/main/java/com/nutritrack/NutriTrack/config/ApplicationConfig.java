//package com.nutritrack.config;
//
//import com.nutritrack.repository.UsuarioRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class ApplicationConfig {
//
//    private final UsuarioRepository usuarioRepository;
//
////    @Bean
////    public UserDetailsService userDetailsService() {
////        return username -> usuarioRepository.findByEmail(username)
////            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
////    }
//
////    @Bean
////    public AuthenticationProvider authenticationProvider() {
////        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
////        authProvider.setUserDetailsService(userDetailsService());
////        authProvider.setPasswordEncoder(passwordEncoder());
////        return authProvider;
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
////        return config.getAuthenticationManager();
////    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//}
