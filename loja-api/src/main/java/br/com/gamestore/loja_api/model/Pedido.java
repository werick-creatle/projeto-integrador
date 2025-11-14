package br.com.gamestore.loja_api.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_pedido")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"usuario", "itens"})
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relacionamento OneoOne sognifica muitos Pedidos podem pertencer a um Usuario.
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDate dataDoPedido;

    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ItemPedido> itens = new HashSet<>();

    public Pedido(Usuario usuario, LocalDate dataDoPedido, BigDecimal valorTotal) {
        this.usuario = usuario;
        this.dataDoPedido = dataDoPedido;
        this.valorTotal = valorTotal;
    }
}
