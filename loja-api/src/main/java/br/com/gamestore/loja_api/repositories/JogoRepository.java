package br.com.gamestore.loja_api.repositories;

import br.com.gamestore.loja_api.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    List<Jogo> findTop4ByOrderByDataLancamentoDesc();

    List<Jogo> findByPlataformaIgnoreCase(String plataforma);
}
