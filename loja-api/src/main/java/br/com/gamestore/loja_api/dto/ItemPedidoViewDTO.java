package br.com.gamestore.loja_api.dto;

import br.com.gamestore.loja_api.model.ItemPedido;
import java.math.BigDecimal;

public record ItemPedidoViewDTO(
        Long jogoId,
        String nomeJogo,
        int quantidade,
        BigDecimal precoUnitario // O preço que foi salvo no momento da compra
) {
    // Construtor auxiliar para facilitar a conversão
    public ItemPedidoViewDTO(ItemPedido item) {
        this(
                item.getJogo().getId(),
                item.getJogo().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario() // Pega o preço salvo no item do pedido
        );
    }
}