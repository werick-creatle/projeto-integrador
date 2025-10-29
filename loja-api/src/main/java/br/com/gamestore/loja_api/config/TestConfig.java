//Eu criei essa classe apenas como teste para testar a o beck end sem o front
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

/*
 * @Configuration: Diz ao Spring que esta é uma classe de configuração.
 * O Spring vai "ler" esta classe ao iniciar para encontrar definições de Beans.
 */
@Configuration
public class TestConfig {

    // Vamos injetar o repositório, pois precisamos dele para salvar os dados
    @Autowired
    private JogoRepository jogoRepository;



    //Injeções para novos teste
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    /*
     * @Bean: Indica que o método abaixo define um "Bean" (um objeto gerenciado pelo Spring).
     * CommandLineRunner: É uma interface que garante que o método 'run()'
     * seja executado assim que a aplicação iniciar.
     */
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {

            // --- Limpa o banco de dados toda vez que a aplicação iniciar ---
            // Isso é ótimo para testes, pois começamos do zero
            jogoRepository.deleteAll();

            // --- Criando nossos jogos de exemplo ---
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

            //Verificação paara saber se o adminin já existe no banco de dados
            if (usuarioRepository.findByLogin("admin@gamestore.com") ==null) {


                // 2. Se não existir, criptografa a senha padrão
                // (NUNCA salve "admin123" direto no banco!
                String senhaAdminCriptografada = passwordEncoder.encode("admin123");

                Usuario admin = new Usuario(
                        null,
                        "admingamestore.com",
                        senhaAdminCriptografada,
                        UsuarioRole.ADMIN
                );
                //Salva o admin no banco de dados
                usuarioRepository.save(admin);
                System.out.println("Usuario administrador cadastrado com secesso, (Login: admin@gamestore.com, Senha: admin123) ");
            }else {
                System.out.println("Usuário admin (admin@gamestore.com) já existe");
            }

        };
    }
}