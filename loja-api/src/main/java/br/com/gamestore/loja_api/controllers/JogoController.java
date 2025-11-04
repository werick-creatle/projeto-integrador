package br.com.gamestore.loja_api.controllers; // Verifique seu pacote

import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.repositories.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * @RestController: Combina @Controller e @ResponseBody.
 * Diz ao Spring que esta classe é um Controller e que os métodos
 * devem retornar JSON (ou XML, etc.) diretamente na resposta HTTP.
 */
@RestController
/*
 * @RequestMapping("/api/jogos"):
 * Define um prefixo de rota para todos os métodos deste controller.
 * Qualquer endpoint aqui dentro começará com '/api/jogos'.
 */
@RequestMapping("/api/jogos")
/*
 * @CrossOrigin("*"):
 * A ANOTAÇÃO MAIS IMPORTANTE PARA O SEU ANGULAR!
 * Permite que requisições de qualquer origem (como localhost:4200)
 * sejam aceitas por esta API.
 */
@CrossOrigin("*")
public class JogoController {

    /*
     * @Autowired: Injeção de Dependência.
     * O Spring vai automaticamente "injetar" (fornecer) uma instância
     * funcional do JogoRepository para nós. Não precisamos dar "new".
     */
    @Autowired
    private JogoRepository jogoRepository;

    /*
     * @GetMapping: Mapeia requisições HTTP GET.
     * Como já temos "/api/jogos" no topo da classe, um @GetMapping
     * sem nada (ou com "/") responde à rota GET /api/jogos.
     * * Este é o endpoint para a sua tela "Catálogos".
     */
    @GetMapping
    public ResponseEntity<List<Jogo>> buscarTodosOsJogos() {
        // 1. Usa o repositório (injetado) para buscar TODOS os jogos no banco.
        List<Jogo> listaDeJogos = jogoRepository.findAll();

        // 2. Retorna uma resposta HTTP 200 (OK) com a lista de jogos no corpo (body).
        // O Spring automaticamente converte a 'listaDeJogos' (Java) para JSON.
        return ResponseEntity.ok(listaDeJogos);
    }

}