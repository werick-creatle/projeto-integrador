package br.com.gamestore.loja_api.config;

import br.com.gamestore.loja_api.model.Carrinho;
import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.model.UsuarioRole;
import br.com.gamestore.loja_api.repositories.JogoRepository;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate; // Import já está aqui, ótimo
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter

@Configuration
public class TestConfig {

    // ... (injeções JogoRepository, UsuarioRepository, PasswordEncoder)
    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {

            // Você comentou o deleteAll(), o que é bom para testes!
            // usuarioRepository.deleteAll();
            // jogoRepository.deleteAll();

            // --- SEÇÃO DOS JOGOS (NENHUMA MUDANÇA AQUI) ---
            // (Todo o seu código de criação dos 24 jogos permanece igual)

            // --- INÍCIO DA SEÇÃO DOS JOGOS ---
            Jogo jogo1 = new Jogo(
                    null, "Elden Ring", "Jogo de RPG de ação aclamado pela crítica.",
                    new BigDecimal("199.90"), "PS4", "RPG",
                    "httpsa://caminho-da-sua-imagem.com/elden-ring.png",
                    LocalDate.parse("2022-02-25")
            );

            Jogo jogo2 = new Jogo(
                    null, "FIFA 23", "Jogo de simulação de futebol.",
                    new BigDecimal("249.90"), "XBOX", "Esporte",
                    "httpsa://caminho-da-sua-imagem.com/fifa-23.png",
                    LocalDate.parse("2022-09-27")
            );

            Jogo jogo3 = new Jogo(
                    null, "God of War Ragnarok", "Aventura épica de Kratos e Atreus.",
                    new BigDecimal("299.90"), "PS4", "Ação",
                    "httpsa://caminho-da-sua-imagem.com/gow-ragnarok.png",
                    LocalDate.parse("2022-11-09")
            );

            Jogo jogo4 = new Jogo(
                    null, "Forza Horizon 5", "Jogo de corrida em mundo aberto no México.",
                    new BigDecimal("249.90"), "XBOX", "Corrida",
                    "httpsa://caminho-da-sua-imagem.com/forza-5.png",
                    LocalDate.parse("2021-11-05")
            );

            List<Jogo> novosJogos = Arrays.asList(
                    new Jogo(null, "Hogwarts Legacy", "Explore o mundo mágico de Harry Potter.", new BigDecimal("299.90"), "PS5", "RPG", "img/hogwarts.png", LocalDate.parse("2023-02-10")),
                    new Jogo(null, "Diablo IV", "Retorno sombrio da franquia de RPG de ação.", new BigDecimal("349.90"), "PC", "RPG", "img/diablo4.png", LocalDate.parse("2023-06-05")),
                    new Jogo(null, "Starfield", "Aventura de RPG espacial da Bethesda.", new BigDecimal("349.90"), "XBOX", "RPG", "img/starfield.png", LocalDate.parse("2023-09-06")),
                    new Jogo(null, "Marvel's Spider-Man 2", "Peter Parker e Miles Morales retornam.", new BigDecimal("349.90"), "PS5", "Ação", "img/spiderman2.png", LocalDate.parse("2023-10-20")),
                    new Jogo(null, "Baldur's Gate 3", "RPG clássico baseado em Dungeons & Dragons.", new BigDecimal("199.90"), "PC", "RPG", "img/bg3.png", LocalDate.parse("2023-08-03")),
                    new Jogo(null, "Cyberpunk 2077", "RPG de ação em Night City.", new BigDecimal("159.90"), "PC", "RPG", "img/cyberpunk.png", LocalDate.parse("2020-12-10")),
                    new Jogo(null, "The Legend of Zelda: Tears of the Kingdom", "Aventura no reino de Hyrule.", new BigDecimal("349.90"), "Switch", "Aventura", "img/zelda_totk.png", LocalDate.parse("2023-05-12")),
                    new Jogo(null, "Red Dead Redemption 2", "Épico de faroeste da Rockstar.", new BigDecimal("199.90"), "PS4", "Ação", "img/rdr2.png", LocalDate.parse("2018-10-26")),
                    new Jogo(null, "The Witcher 3: Wild Hunt", "RPG de mundo aberto de Geralt de Rivia.", new BigDecimal("129.90"), "PC", "RPG", "img/witcher3.png", LocalDate.parse("2015-05-19")),
                    new Jogo(null, "Hades", "Roguelike de mitologia grega.", new BigDecimal("89.90"), "Switch", "Roguelike", "img/hades.png", LocalDate.parse("2020-09-17")),
                    new Jogo(null, "Resident Evil 4 (Remake)", "Remake do clássico de terror.", new BigDecimal("249.90"), "PS5", "Terror", "img/re4.png", LocalDate.parse("2023-03-24")),
                    new Jogo(null, "Street Fighter 6", "Novo capítulo da lendária série de luta.", new BigDecimal("299.90"), "PS5", "Luta", "img/sf6.png", LocalDate.parse("2023-06-02")),
                    new Jogo(null, "Mortal Kombat 1", "Reboot da franquia de luta.", new BigDecimal("319.90"), "XBOX", "Luta", "img/mk1.png", LocalDate.parse("2023-09-19")),
                    new Jogo(null, "Alan Wake 2", "Sequência do aclamado terror psicológico.", new BigDecimal("279.90"), "PC", "Terror", "img/alanwake2.png", LocalDate.parse("2023-10-27")),
                    new Jogo(null, "Lies of P", "RPG de ação 'souls-like' do Pinóquio.", new BigDecimal("249.90"), "PC", "RPG", "img/liesofp.png", LocalDate.parse("2023-09-18")),
                    new Jogo(null, "Final Fantasy XVI", "Novo épico de RPG da Square Enix.", new BigDecimal("349.90"), "PS5", "RPG", "img/ff16.png", LocalDate.parse("2023-06-22")),
                    new Jogo(null, "Sea of Stars", "RPG indie inspirado nos clássicos.", new BigDecimal("129.90"), "Switch", "RPG", "img/seaofstars.png", LocalDate.parse("2023-08-29")),
                    new Jogo(null, "Armored Core VI", "Ação com mechas da FromSoftware.", new BigDecimal("299.90"), "XBOX", "Ação", "img/ac6.png", LocalDate.parse("2023-08-25")),
                    new Jogo(null, "Star Wars Jedi: Survivor", "Continuação da jornada de Cal Kestis.", new BigDecimal("319.90"), "PS5", "Ação", "img/jedi_survivor.png", LocalDate.parse("2023-04-28")),
                    new Jogo(null, "Assassin's Creed Mirage", "Retorno às raízes da franquia.", new BigDecimal("239.90"), "XBOX", "Ação", "img/ac_mirage.png", LocalDate.parse("2023-10-05"))
            );

            List<Jogo> todosOsJogos = new ArrayList<>(Arrays.asList(jogo1, jogo2, jogo3, jogo4));
            todosOsJogos.addAll(novosJogos);

            // Apenas salve os jogos se o repositório estiver vazio
            // (Melhoria para evitar duplicatas já que o deleteAll() está comentado)
            if (jogoRepository.count() == 0) {
                jogoRepository.saveAll(todosOsJogos);
                System.out.println(">>> BANCO DE DADOS POPULADO COM 24 JOGOS! <<<");
            } else {
                System.out.println(">>> BANCO DE JOGOS JÁ POSSUI DADOS. <<<");
            }


            // --- CRIAÇÃO DO USUÁRIO ADMIN (ESSA É A PARTE CORRIGIDA) ---
            System.out.println(">>> Verificando/Criando usuário ADMIN padrão...");
            if (usuarioRepository.findByLogin("admin@gamestore.com") == null) {
                String senhaAdminCriptografada = passwordEncoder.encode("admin1G23"); // Mudei a senha para "admin1G23"


                Usuario admin = new Usuario(
                        null,
                        "admin@gamestore.com",
                        senhaAdminCriptografada,
                        UsuarioRole.ADMIN,
                        "Administrador da Loja",
                        LocalDate.parse("2000-01-01"),
                        null
                );
                Carrinho carrinhoAdmin = new Carrinho(admin);
                admin.setCarrinho(carrinhoAdmin);


                usuarioRepository.save(admin);
                System.out.println("Usuário administrador cadastrado com sucesso (Login: admin@gamestore.com, Senha: admin1G23)");
            } else {
                System.out.println("Usuário admin (admin@gamestore.com) já existe");
            }
        };
    }
}