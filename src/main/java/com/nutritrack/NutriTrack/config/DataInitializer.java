package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.entity.NivelAtividade;
import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import com.nutritrack.NutriTrack.entity.Role;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente responsável por inicializar dados essenciais no banco de dados
 * após a aplicação ser iniciada.
 * <p>
 * Garante que dados básicos, como um usuário administrador padrão, existam
 * na base de dados, prevenindo problemas de acesso inicial.
 * <p>
 * Utiliza {@link PostConstruct} para executar o método de inicialização
 * automaticamente e {@link Transactional} para garantir a atomicidade
 * da operação no banco.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Inicializa os dados essenciais da aplicação.
     * <p>
     * Este método é executado uma única vez após a construção do bean.
     * <p>
     * A lógica verifica se um usuário administrador padrão (com o email "admin@email.com")
     * já existe no banco de dados. Se não existir, ele cria um novo registro de usuário
     * com o {@link Role#ROLE_ADMIN} e codifica a senha antes de salvar.
     *
     * @see PostConstruct
     * @see Transactional
     */
    @PostConstruct
    @Transactional
    public void initData(){


        if (usuarioRepository.findByEmail("admin@email.com").isEmpty()){

            Usuario userAdmin = new Usuario();

            userAdmin.setNome("admin");
            userAdmin.setEmail("admin@email.com");
            userAdmin.setSenhaHash(passwordEncoder.encode("senhaforte"));
            userAdmin.setAlturaM(BigDecimal.valueOf(1.55));
            userAdmin.setDataNascimento(LocalDate.parse("2000-10-09"));
            userAdmin.setNivelAtividade(NivelAtividade.LEVE);
            userAdmin.setObjetivoUsuario(ObjetivoUsuario.PERDER_PESO);
            userAdmin.setRole(Role.ROLE_ADMIN);

            usuarioRepository.save(userAdmin);
            System.out.println("Dados iniciais criados com sucesso!");
        }
    }
}
