package br.com.gamestore.loja_api.dto;

import br.com.gamestore.loja_api.model.ItemDoCarrinho;
import java.math.BigDecimal;

public record ItemCarrinhoViewDTO(
        Long itemId,

        Long jogoId,
        String nomeJogo,
        String urlImagemCapa,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    // Construtor auxiliar para facilitar a conversão
    public ItemCarrinhoViewDTO(ItemDoCarrinho item){
        this(
                item.getId(),
                item.getJogo().getId(),
                item.getJogo().getNome(),
                item.getJogo().getUrlImagemCapa(),
                item.getQuantidade(),
                item.getJogo().getPreco(),
                //Calcular o subtotal: (Preço * quantidade)
                item.getJogo().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()))
        );
    }
}
