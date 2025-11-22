package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.dto.ItemAtualizarDTO;
import br.com.gamestore.loja_api.dto.ItemCarrinhoViewDTO;
import br.com.gamestore.loja_api.dto.CarrinhoViewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import br.com.gamestore.loja_api.dto.ItemAdicionarDTO;
import br.com.gamestore.loja_api.model.Carrinho;
import br.com.gamestore.loja_api.model.ItemDoCarrinho;
import br.com.gamestore.loja_api.model.Jogo;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.repositories.ItemDoCarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarrinhoService {

    @Autowired
    private ItemDoCarrinhoRepository itemDoCarrinhoRepository;

    @Autowired
    private JogoService jogoService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public void removerDoCarrinho(Long itemId, Usuario usuario) {
        // Aqui eu pego o carrinho associado ao usuário logado
        Carrinho carrinho = usuario.getCarrinho();

        // Aqui eu busco o item pelo ID no banco de dados; o retorno é um Optional, que pode ou não conter o item
        Optional<ItemDoCarrinho> optionalItem = itemDoCarrinhoRepository.findById(itemId);

        ItemDoCarrinho itemParaExcluir;
        if (optionalItem.isPresent()) {
            itemParaExcluir = optionalItem.get();
        } else {
            // Se o Optional estiver vazio, significa que o item não existe, então lança exceção 404 (não encontrado)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado");
        }

        // Verifica se o item realmente pertence ao carrinho do usuário logado
        if (!itemParaExcluir.getCarrinho().getId().equals(carrinho.getId())) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permição para remover esse item do carrinho");
        }
        //Se passar na verificação de segurança o metodo exclui o objeto
        itemDoCarrinhoRepository.delete(itemParaExcluir);


    }


    public void adicionarAoCarrinho(ItemAdicionarDTO dados, Usuario usuario) {
        Carrinho carrinho = usuario.getCarrinho();

        // Aqui eu busco o jogo pelo ID informado no DTO, garantindo que o jogo realmente existe
        Jogo jogo = jogoService.buscarPorId(dados.jogoId());

        if (jogo.getQuantidadeEstoque() < dados.quantidade()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estoque insuficiente. Quantidade disponível: " + jogo.getQuantidadeEstoque());
        }


        // Aqui eu verifico se o jogo já existe no carrinho do usuário
        // O método 'findByCarrinhoIdAndJogoId' procura um item com o mesmo jogo dentro do mesmo carrinho
        Optional<ItemDoCarrinho> optionalItem = itemDoCarrinhoRepository.findByCarrinhoIdAndJogoId(carrinho.getId(), jogo.getId());


        // Se o item já estiver no carrinho, apenas atualizo a quantidade
        if (optionalItem.isPresent()) {

            ItemDoCarrinho itemExistente = optionalItem.get();

            int novaQuantidade = itemExistente.getQuantidade() + dados.quantidade();
            if (jogo.getQuantidadeEstoque() < novaQuantidade) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente. Você já tem: " + itemExistente.getQuantidade() +
                                "no carrinho. Quantidade disponível: " + jogo.getQuantidadeEstoque());
            }


            // Aqui eu somo a nova quantidade à que já estava no carrinho
            itemExistente.setQuantidade(itemExistente.getQuantidade() + dados.quantidade());

            // E salvo a alteração no banco de dados
            itemDoCarrinhoRepository.save(itemExistente);
        } else {
            ItemDoCarrinho novoitem = new ItemDoCarrinho(carrinho, jogo, dados.quantidade());
            itemDoCarrinhoRepository.save(novoitem);
        }
    }

    @Transactional(readOnly = true)
    public CarrinhoViewDTO verCarrinho(String loginUsuario) {
        // Aqui eu busco o usuário pelo login informado
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        // Aqui eu pego o carrinho que pertence a esse usuário
        Carrinho carrinho = usuario.getCarrinho();


        // Aqui eu pego todos os itens do carrinho e converto cada um para um DTO de visualização (ItemCarrinhoViewDTO)
        // O 'stream' serve para percorrer a lista de itens e o 'map' cria um novo objeto DTO para cada item encontrado
        List<ItemCarrinhoViewDTO> itensDTO = carrinho.getItens().stream()
                .map(item -> new ItemCarrinhoViewDTO(item))
                .collect(Collectors.toList());


        // Aqui eu calculo o total do carrinho somando o subtotal de cada item
        // O 'reduce' começa com ZERO e vai somando o valor de cada item
        BigDecimal total = itensDTO.stream()
                .map(item -> item.subtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Por fim, retorno um novo objeto CarrinhoViewDTO contendo a lista de itens e o valor total do carrinho
        return new CarrinhoViewDTO(itensDTO, total);
    }


    // Mude o retorno do método para o seu DTO
    public ItemCarrinhoViewDTO atualizarQuantidadeItem(Long itemId, ItemAtualizarDTO dados, Usuario usuario) {
        Carrinho carrinho = usuario.getCarrinho();

        // Busca o item (usando a forma simplificada)
        ItemDoCarrinho itemParaAtualizar = itemDoCarrinhoRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));

        // Verifica se o item é do usuário mesmo
        if (!itemParaAtualizar.getCarrinho().getId().equals(carrinho.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar esse item");
        }

        Jogo jogo = itemParaAtualizar.getJogo();
        if (jogo.getQuantidadeEstoque() < dados.quantidade()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estoque insuficiente. Quantidade disponível: " + jogo.getQuantidadeEstoque());
        }

        // Atualiza a quantidade
        itemParaAtualizar.setQuantidade(dados.quantidade());

        // Salva e captura o objeto salvo atualizado
        ItemDoCarrinho itemSalvo = itemDoCarrinhoRepository.save(itemParaAtualizar);

        // AQUI A MÁGICA: Retorna o DTO.
        // O construtor do DTO vai recalcular o subtotal com a nova quantidade.
        return new ItemCarrinhoViewDTO(itemSalvo);
    }
}
