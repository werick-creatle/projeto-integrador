package br.com.gamestore.loja_api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_item_carrinho")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"carrinho", "jogo"})
public class ItemDoCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrinho_tb")
    private Carrinho carrinho;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    private int quantidade;

    public ItemDoCarrinho(Carrinho carrinho, Jogo jogo, int quantidade) {
        this.carrinho = carrinho;
        this.jogo = jogo;
        this.quantidade = quantidade;
    }
}
