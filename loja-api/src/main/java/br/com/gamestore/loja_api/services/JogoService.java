package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.dto.JogoCadastroDTO;
import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.repositories.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class JogoService {

    @Autowired
    private JogoRepository jogoRepository;

    public Page<Jogo> listarTodos(Pageable  pageable) {
        return jogoRepository.findAll(pageable);
    }

    public Jogo buscarPorId(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogo não encontrado"));
    }

    public Jogo cadastrarNovoJogo(JogoCadastroDTO dados) {
        if (jogoRepository.existsByNomeIgnoreCase(dados.nome())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: Já existe um jogo cadastrado com este nome.");
        }

        //Conversão DTO em Entidade
        Jogo novoJogo = new Jogo(
                null, dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.plataforma(),
                dados.genero(),
                dados.urlImagemCapa(),
                dados.dataLancamento(),
                dados.quantidadeEstoque()
        );

        return jogoRepository.save(novoJogo);
    }

    public Jogo atualizarJogo(Long id, JogoCadastroDTO dados) {
        Jogo jogoExistente = this.buscarPorId(id);

        jogoExistente.setNome(dados.nome());
        jogoExistente.setDescricao(dados.descricao());
        jogoExistente.setPreco(dados.preco());
        jogoExistente.setPlataforma(dados.plataforma());
        jogoExistente.setGenero(dados.genero());
        jogoExistente.setUrlImagemCapa(dados.urlImagemCapa());
        jogoExistente.setDataLancamento(dados.dataLancamento());

        return jogoRepository.save(jogoExistente);
    }

    public void deletarJogo(Long id) {
        this.buscarPorId(id);
        jogoRepository.deleteById(id);
    }

    public List<Jogo> buscarNovidades() {
        return jogoRepository.findTop4ByOrderByDataLancamentoDesc();
    }

    public List<Jogo> buscarPorPlataforma(String plataforma) {
        return jogoRepository.findByPlataformaIgnoreCase(plataforma);
    }
}