package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.model.*;
import br.com.gamestore.loja_api.repositories.ItemDoCarrinhoRepository;
import br.com.gamestore.loja_api.repositories.ItemPedidoRepository;
import br.com.gamestore.loja_api.repositories.PedidoRepository;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.gamestore.loja_api.repositories.CarrinhoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Transactional
    public Pedido finalizarCompra(String loginUsuario) {

        Usuario usuario = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        Carrinho carrinho = usuario.getCarrinho();

        //Buscar o item do carrinho
        Set<ItemDoCarrinho> itensDoCarrinhos = carrinho.getItens();

        //verifico se o carrinho esta vazio
        if (itensDoCarrinhos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seu carrinho esta vazio");
        }

        //Esse bloco de codigo calcula o total do carrinho
        BigDecimal total = itensDoCarrinhos.stream()
                .map(item -> item.getJogo().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        //Esse metodo cria o recido
        Pedido novoPedido = new Pedido(usuario, LocalDate.now(), total);

        //Aqui eu copio os itens do carrinho para o recibo
        Set<ItemPedido> itensDoPedido = new HashSet<>();

        for (ItemDoCarrinho itemDoCarrinho : itensDoCarrinhos) {
            ItemPedido novoItemPedido = new ItemPedido(
                    novoPedido,
                    itemDoCarrinho.getJogo(),
                    itemDoCarrinho.getQuantidade(),
                    itemDoCarrinho.getJogo().getPreco()// Salva o preço
            );
            itensDoPedido.add(novoItemPedido);
        }

        //Aqui eu amarro a lista de itens ao pedido
        novoPedido.setItens(itensDoPedido);

        //Aqui eu estou salvando o pedido
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);


        carrinho.getItens().clear();//Esvazia a coleção

        carrinhoRepository.saveAndFlush(carrinho);//Salva imediatamente as alterações no banco


        return pedidoSalvo;//Essa parte faz o sistema retornar ao pedido q foi salvo

    }
}
