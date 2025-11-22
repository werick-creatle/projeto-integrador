package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.dto.PedidoViewDTO;
import br.com.gamestore.loja_api.model.*;
import br.com.gamestore.loja_api.repositories.PedidoRepository;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import br.com.gamestore.loja_api.repositories.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    // --- NOVO MÉTODO (O QUE FALTAVA PARA O GET FUNCIONAR) ---
    public PedidoViewDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return new PedidoViewDTO(pedido);
    }

    // --- SEU MÉTODO DE FINALIZAR COMPRA (MANTIDO IGUAL) ---
    @Transactional
    public Pedido finalizarCompra(String loginUsuario){

        Usuario usuario = (Usuario) usuarioRepository.findByLogin(loginUsuario);
        Carrinho carrinho = usuario.getCarrinho();

        //Buscar o item do carrinho
        Set<ItemDoCarrinho> itensDoCarrinhos = carrinho.getItens();

        //verifico se o carrinho esta vazio
        if (itensDoCarrinhos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seu carrinho esta vazio");
        }

        // Começa o total zerado; será calculado DENTRO do loop
        BigDecimal total = BigDecimal.ZERO;

        //Esse metodo cria o recido (com total nulo por enquanto)
        Pedido novoPedido = new Pedido(usuario, LocalDate.now(), null);

        //Aqui eu copio os itens do carrinho para o recibo
        Set<ItemPedido> itensDoPedido = new HashSet<>();

        for (ItemDoCarrinho itemDoCarrinho : itensDoCarrinhos){

            // --- LÓGICA DE ESTOQUE ---
            Jogo jogo = itemDoCarrinho.getJogo();

            // 1. Verifica se a quantidade no carrinho é maior que o estoque
            if (jogo.getQuantidadeEstoque() < itemDoCarrinho.getQuantidade()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque esgotado para o item: " + jogo.getNome() +
                                ". Disponível: " + jogo.getQuantidadeEstoque());
            }

            // 2. Dá baixa no estoque
            int novoEstoque = jogo.getQuantidadeEstoque() - itemDoCarrinho.getQuantidade();
            jogo.setQuantidadeEstoque(novoEstoque);
            // --- FIM DA LÓGICA DE ESTOQUE ---

            ItemPedido novoItemPedido = new ItemPedido(
                    novoPedido,
                    jogo,
                    itemDoCarrinho.getQuantidade(),
                    jogo.getPreco()// Salva o preço
            );
            itensDoPedido.add(novoItemPedido);

            // Soma o total
            total = total.add(jogo.getPreco().multiply(BigDecimal.valueOf(itemDoCarrinho.getQuantidade())));
        }

        //Aquyi eu amarro a lista de itens ao pedido
        novoPedido.setItens(itensDoPedido);

        // Agora que o loop acabou, define o total calculado
        novoPedido.setValorTotal(total);

        //Aqui eu estou salvando o pedido
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        //Aqui eu limpo o carrinho original
        carrinho.getItens().clear();//Esvazia a coleção
        carrinhoRepository.saveAndFlush(carrinho);//Salva imediatamente as alterações no banco

        //Essa parte faz o sistema retornar ao pedido q foi salvo
        return pedidoSalvo;
    }
}