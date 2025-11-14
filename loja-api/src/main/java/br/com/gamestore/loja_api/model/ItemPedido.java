package br.com.gamestore.loja_api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_item_pedido")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"pedido", "jogo"})
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemPedido(Pedido pedido, Jogo jogo, int quantidade, BigDecimal precoUnitario) {
        this.pedido = pedido;
        this.jogo = jogo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

}
