package br.com.gamestore.loja_api.config;

import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.model.UsuarioRole;
import br.com.gamestore.loja_api.repositories.JogoRepository;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class TestConfig {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {

            //usuarioRepository.deleteAll();
            //jogoRepository.deleteAll();

            Jogo jogo1 = new Jogo(
                    null, // ID é nulo, pois o banco vai gerar (AUTO_INCREMENT)
                    "Elden Ring",
                    "Jogo de RPG de ação aclamado pela crítica.",
                    new BigDecimal("199.90"),
                    "PS4",
                    "RPG",
                    "https://caminho-da-sua-imagem.com/elden-ring.png",
                    LocalDate.parse("2022-02-25")
            );

            Jogo jogo2 = new Jogo(
                    null,
                    "FIFA 23",
                    "Jogo de simulação de futebol.",
                    new BigDecimal("249.90"),
                    "XBOX",
                    "Esporte",
                    "https://caminho-da-sua-imagem.com/fifa-23.png",
                    LocalDate.parse("2022-09-27")
            );

            Jogo jogo3 = new Jogo(
                    null,
                    "God of War Ragnarok",
                    "Aventura épica de Kratos e Atreus.",
                    new BigDecimal("299.90"),
                    "PS4",
                    "Ação",
                    "https://caminho-da-sua-imagem.com/gow-ragnarok.png",
                    LocalDate.parse("2022-11-09")
            );

            Jogo jogo4 = new Jogo(
                    null,
                    "Forza Horizon 5",
                    "Jogo de corrida em mundo aberto no México.",
                    new BigDecimal("249.90"),
                    "XBOX",
                    "Corrida",
                    "https://caminho-da-sua-imagem.com/forza-5.png",
                    LocalDate.parse("2021-11-05")
            );

            // --- Salvando a lista de jogos no banco de dados ---
            // O 'saveAll' salva todos os objetos da lista de uma vez
            jogoRepository.saveAll(Arrays.asList(jogo1, jogo2, jogo3, jogo4));
            System.out.println(">>> BANCO DE DADOS POPULADO COM SUCESSO! <<<");

            System.out.println(">>> Verificando/Criando usuário ADMIN padrão...");

            // Verificação para saber se o admin já existe no banco de dados
            if (usuarioRepository.findByLogin("admin@gamestore.com") == null) {

                // 2. Se não existir, criptografa a senha padrão
                // (NUNCA salve "admin123" direto no banco!)
                String senhaAdminCriptografada = passwordEncoder.encode("admin123");

                // --- Correção do erro de digitação ---
                // Faltava o '@' no email do admin
                Usuario admin = new Usuario(
                        null,
                        "admin@gamestore.com",
                        senhaAdminCriptografada,
                        UsuarioRole.ADMIN
                );

                // Salva o admin no banco de dados
                usuarioRepository.save(admin);
                System.out.println("Usuário administrador cadastrado com sucesso (Login: admin@gamestore.com, Senha: admin123)");

            } else {
                System.out.println("Usuário admin (admin@gamestore.com) já existe");
            }
        };
    }
}
