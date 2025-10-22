package com.nutritrack.NutriTrack.config;

import com.nutritrack.NutriTrack.entity.*;
import com.nutritrack.NutriTrack.repository.AlimentoRepository;
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
import java.util.Arrays;
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

    private final AlimentoRepository alimentoRepository;

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
        if (alimentoRepository.count() == 1 || alimentoRepository.count() == 0) {

            List<Alimento> alimentos = Arrays.asList(
                    // 1. Frango (peito cozido, 100g)
                    Alimento.builder()
                            .nome("Frango (peito cozido, 100g)")
                            .calorias(new BigDecimal("165.000"))
                            .proteinasG(new BigDecimal("31.000"))
                            .carboidratosG(new BigDecimal("0.000"))
                            .gordurasG(new BigDecimal("3.600"))
                            .build(),

                    // 2. Arroz Branco (cozido, 100g)
                    Alimento.builder()
                            .nome("Arroz Branco (cozido, 100g)")
                            .calorias(new BigDecimal("130.000"))
                            .proteinasG(new BigDecimal("2.700"))
                            .carboidratosG(new BigDecimal("28.200"))
                            .gordurasG(new BigDecimal("0.300"))
                            .build(),

                    // 3. Feijão Carioca (cozido, 100g)
                    Alimento.builder()
                            .nome("Feijão Carioca (cozido, 100g)")
                            .calorias(new BigDecimal("76.000"))
                            .proteinasG(new BigDecimal("4.800"))
                            .carboidratosG(new BigDecimal("13.600"))
                            .gordurasG(new BigDecimal("0.500"))
                            .build(),

                    // 4. Ovo Cozido (1 unidade grande)
                    Alimento.builder()
                            .nome("Ovo Cozido (1 unidade grande)")
                            .calorias(new BigDecimal("78.000"))
                            .proteinasG(new BigDecimal("6.300"))
                            .carboidratosG(new BigDecimal("0.600"))
                            .gordurasG(new BigDecimal("5.300"))
                            .build(),

                    // 5. Maçã (1 unidade média)
                    Alimento.builder()
                            .nome("Maçã (1 unidade média)")
                            .calorias(new BigDecimal("95.000"))
                            .proteinasG(new BigDecimal("0.500"))
                            .carboidratosG(new BigDecimal("25.000"))
                            .gordurasG(new BigDecimal("0.300"))
                            .build(),

                    // 6. Banana Prata (1 unidade média)
                    Alimento.builder()
                            .nome("Banana Prata (1 unidade média)")
                            .calorias(new BigDecimal("105.000"))
                            .proteinasG(new BigDecimal("1.300"))
                            .carboidratosG(new BigDecimal("27.000"))
                            .gordurasG(new BigDecimal("0.400"))
                            .build(),

                    // 7. Leite Integral (200ml)
                    Alimento.builder()
                            .nome("Leite Integral (200ml)")
                            .calorias(new BigDecimal("122.000"))
                            .proteinasG(new BigDecimal("6.600"))
                            .carboidratosG(new BigDecimal("9.400"))
                            .gordurasG(new BigDecimal("6.000"))
                            .build(),

                    // 8. Iogurte Natural Integral (1 pote 170g)
                    Alimento.builder()
                            .nome("Iogurte Natural Integral (1 pote 170g)")
                            .calorias(new BigDecimal("110.000"))
                            .proteinasG(new BigDecimal("6.000"))
                            .carboidratosG(new BigDecimal("8.000"))
                            .gordurasG(new BigDecimal("6.000"))
                            .build(),

                    // 9. Pão Francês (1 unidade 50g)
                    Alimento.builder()
                            .nome("Pão Francês (1 unidade 50g)")
                            .calorias(new BigDecimal("140.000"))
                            .proteinasG(new BigDecimal("4.500"))
                            .carboidratosG(new BigDecimal("27.000"))
                            .gordurasG(new BigDecimal("1.500"))
                            .build(),

                    // 10. Batata Doce (cozida, 100g)
                    Alimento.builder()
                            .nome("Batata Doce (cozida, 100g)")
                            .calorias(new BigDecimal("86.000"))
                            .proteinasG(new BigDecimal("1.600"))
                            .carboidratosG(new BigDecimal("20.100"))
                            .gordurasG(new BigDecimal("0.100"))
                            .build(),

                    // 11. Salmão (cozido, 100g)
                    Alimento.builder()
                            .nome("Salmão (cozido, 100g)")
                            .calorias(new BigDecimal("208.000"))
                            .proteinasG(new BigDecimal("20.400"))
                            .carboidratosG(new BigDecimal("0.000"))
                            .gordurasG(new BigDecimal("13.400"))
                            .build(),

                    // 12. Brócolis (cozido, 100g)
                    Alimento.builder()
                            .nome("Brócolis (cozido, 100g)")
                            .calorias(new BigDecimal("35.000"))
                            .proteinasG(new BigDecimal("2.400"))
                            .carboidratosG(new BigDecimal("7.200"))
                            .gordurasG(new BigDecimal("0.400"))
                            .build(),

                    // 13. Aveia (flocos, 30g)
                    Alimento.builder()
                            .nome("Aveia (flocos, 30g)")
                            .calorias(new BigDecimal("117.000"))
                            .proteinasG(new BigDecimal("4.100"))
                            .carboidratosG(new BigDecimal("20.000"))
                            .gordurasG(new BigDecimal("2.100"))
                            .build(),

                    // 14. Manteiga (1 colher de sopa, 10g)
                    Alimento.builder()
                            .nome("Manteiga (1 colher de sopa, 10g)")
                            .calorias(new BigDecimal("72.000"))
                            .proteinasG(new BigDecimal("0.100"))
                            .carboidratosG(new BigDecimal("0.000"))
                            .gordurasG(new BigDecimal("8.100"))
                            .build(),

                    // 15. Queijo Muçarela (fatia, 30g)
                    Alimento.builder()
                            .nome("Queijo Muçarela (fatia, 30g)")
                            .calorias(new BigDecimal("93.000"))
                            .proteinasG(new BigDecimal("7.500"))
                            .carboidratosG(new BigDecimal("0.600"))
                            .gordurasG(new BigDecimal("7.100"))
                            .build(),

                    // 16. Carne Bovina (patinho cozido, 100g)
                    Alimento.builder()
                            .nome("Carne Bovina (patinho cozido, 100g)")
                            .calorias(new BigDecimal("179.000"))
                            .proteinasG(new BigDecimal("29.000"))
                            .carboidratosG(new BigDecimal("0.000"))
                            .gordurasG(new BigDecimal("6.200"))
                            .build(),

                    // 17. Azeite de Oliva Extra Virgem (1 colher de sopa, 13g)
                    Alimento.builder()
                            .nome("Azeite de Oliva Extra Virgem (1 colher de sopa, 13g)")
                            .calorias(new BigDecimal("117.000"))
                            .proteinasG(new BigDecimal("0.000"))
                            .carboidratosG(new BigDecimal("0.000"))
                            .gordurasG(new BigDecimal("13.000"))
                            .build(),

                    // 18. Cenoura (crua, 100g)
                    Alimento.builder()
                            .nome("Cenoura (crua, 100g)")
                            .calorias(new BigDecimal("41.000"))
                            .proteinasG(new BigDecimal("0.900"))
                            .carboidratosG(new BigDecimal("9.600"))
                            .gordurasG(new BigDecimal("0.200"))
                            .build(),

                    // 19. Laranja Pêra (1 unidade média)
                    Alimento.builder()
                            .nome("Laranja Pêra (1 unidade média)")
                            .calorias(new BigDecimal("62.000"))
                            .proteinasG(new BigDecimal("1.200"))
                            .carboidratosG(new BigDecimal("15.400"))
                            .gordurasG(new BigDecimal("0.200"))
                            .build(),

                    // 20. Amendoim Torrado (30g)
                    Alimento.builder()
                            .nome("Amendoim Torrado (30g)")
                            .calorias(new BigDecimal("170.000"))
                            .proteinasG(new BigDecimal("7.500"))
                            .carboidratosG(new BigDecimal("5.500"))
                            .gordurasG(new BigDecimal("13.000"))
                            .build()
            );

            alimentoRepository.saveAll(alimentos);
            System.out.println("20 dados de Alimentos iniciais criados com sucesso!");
        }
    }
}