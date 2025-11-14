package br.com.gamestore.loja_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "tb_carrinho")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"usuario", "itens"})
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "carrinho")
    private Usuario usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ItemDoCarrinho> itens = new HashSet<>();

    public Carrinho(Usuario usuario) {
        this.usuario = usuario;
    }
}
