package br.com.gamestore.loja_api.controllers;


import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import br.com.gamestore.loja_api.dto.JogoCadastroDTO;
import org.springframework.http.HttpStatus;
import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.repositories.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/jogos")
@CrossOrigin("*")
public class JogoController {


    @Autowired
    private JogoRepository jogoRepository;



    @PostMapping
    public ResponseEntity<?> cadastrarNovoJogo(@RequestBody JogoCadastroDTO dados) {
        if (jogoRepository.existsByNomeIgnoreCase(dados.nome())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Já existe um jogo cadastrado com esse nome.");
        }
        //Agora vou criar uma nova entidade a partir dos dados dos dados do DTO
        Jogo jogoNovo = new Jogo(
                null,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.plataforma(),
                dados.genero(),
                dados.urlImagemCapa(),
                dados.dataLancamento()
        );
        jogoRepository.save(jogoNovo);//Aqui o metodo esta salvando o jogo no banco

        return ResponseEntity.status(HttpStatus.CREATED).body(jogoNovo);
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> buscarTodosOsJogos() {
        List<Jogo> listaDeJogos = jogoRepository.findAll();


        return ResponseEntity.ok(listaDeJogos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarJogoPorId(@PathVariable Long id) {
        Optional<Jogo> optionalJogo = jogoRepository.findById(id);

        //Verificação para saber se o jogo foi encontrado
        if (optionalJogo.isPresent()) {
            Jogo jogoEncontrado = optionalJogo.get(); // se achar o jogo retorna o jogo
            return ResponseEntity.ok(jogoEncontrado);
        } else {
            return ResponseEntity.notFound().build();//Se não achar retorna o status da requisição
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogo> atualizarJogo(@PathVariable Long id, @RequestBody JogoCadastroDTO dados ){

        //Tento encontrar o jogo a ser atualizado
        Optional<Jogo> optionalJogo = jogoRepository.findById(id);

        if (optionalJogo.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Jogo jogoExistente = optionalJogo.get();

        //Atualiza o campo jogo se o jogo existir
        jogoExistente.setNome(dados.nome());
        jogoExistente.setDescricao(dados.descricao());
        jogoExistente.setPreco(dados.preco());
        jogoExistente.setPlataforma(dados.plataforma());
        jogoExistente.setGenero(dados.genero());
        jogoExistente.setUrlImagemCapa(dados.urlImagemCapa());
        jogoExistente.setDataLancamento(dados.dataLancamento());

        Jogo jogoAtualizado = jogoRepository.save(jogoExistente); // Salva as alterações

        return ResponseEntity.ok(jogoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Jogo> deletarjogo(@PathVariable Long id){
        Optional<Jogo> optionalJogo = jogoRepository.findById(id);

        if (optionalJogo.isEmpty()){
            return  ResponseEntity.notFound().build();
        }

        jogoRepository.deleteById(id);//Apaga o jogo

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/novidades")
    public ResponseEntity<List<Jogo>> buscarnovidades(){
        List<Jogo> novidades = jogoRepository.findTop4ByOrderByDataLancamentoDesc();

        return ResponseEntity.ok(novidades);

    }

}