package br.com.gamestore.loja_api.dto;

import br.com.gamestore.loja_api.model.Pedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoViewDTO(
        Long pedidoId,
        LocalDate dataDoPedido,
        BigDecimal valorTotal,
        List<ItemPedidoViewDTO> itens
) {
    // Construtor auxiliar para facilitar a conversão
    public PedidoViewDTO(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getDataDoPedido(),
                pedido.getValorTotal(),
                // Converte a Set<ItemPedido> (Entidade)
                // para List<ItemPedidoViewDTO> (DTO)
                pedido.getItens().stream()
                        .map(item -> new ItemPedidoViewDTO(item))
                        .collect(Collectors.toList())
        );
    }
}