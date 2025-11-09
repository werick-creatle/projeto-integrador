package br.com.gamestore.loja_api.controllers;

import br.com.gamestore.loja_api.dto.JogoCadastroDTO;
import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.services.JogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/jogos")
@CrossOrigin("*")
public class JogoController {


    @Autowired
    private JogoService jogoService;

    // 1. Buscar Todos
    @GetMapping
    public ResponseEntity<Page<Jogo>> buscarTodosOsJogos(Pageable pageable) {
        // O controller só chama o service e retorna OK
        Page<Jogo> paginaDeJogos = jogoService.listarTodos(pageable);
        return ResponseEntity.ok(paginaDeJogos);
    }

    // 2. Buscar por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarJogoPorId(@PathVariable Long id) {
        try {
            Jogo jogoEncontrado = jogoService.buscarPorId(id);
            return ResponseEntity.ok(jogoEncontrado);
        } catch (ResponseStatusException ex) {
            // Se o service lançou 404, o controller repassa
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Cadastrar Novo
    @PostMapping
    public ResponseEntity<?> cadastrarNovoJogo(@Valid @RequestBody JogoCadastroDTO dados) {
        try {
            Jogo jogoSalvo = jogoService.cadastrarNovoJogo(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
        } catch (ResponseStatusException ex) {
            // Se o service lançou 400 (Bad Request), o controller repassa
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // 4. Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarJogo(@PathVariable Long id, @Valid @RequestBody JogoCadastroDTO dados) {
        try {
            Jogo jogoAtualizado = jogoService.atualizarJogo(id, dados);
            return ResponseEntity.ok(jogoAtualizado);
        } catch (ResponseStatusException ex) {
            // Pode ser 404 (se não achou) ou 400 (se o DTO for inválido no futuro)
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getReason());
        }
    }

    // 5. Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarJogo(@PathVariable Long id) {
        try {
            jogoService.deletarJogo(id);
            // Retorna 204 No Content (sucesso, sem corpo)
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            // Se o service lançou 404, o controller repassa
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Novidades
    @GetMapping("/novidades")
    public ResponseEntity<List<Jogo>> buscarNovidades() {
        List<Jogo> novidades = jogoService.buscarNovidades();
        return ResponseEntity.ok(novidades);
    }

    // 7. Filtro Plataforma
    @GetMapping("/plataforma/{plataforma}")
    public ResponseEntity<List<Jogo>> buscarPorPlataforma(@PathVariable String plataforma) {
        List<Jogo> jogosPorPlataforma = jogoService.buscarPorPlataforma(plataforma);
        return ResponseEntity.ok(jogosPorPlataforma);
    }
}