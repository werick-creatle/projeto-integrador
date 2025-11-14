package br.com.gamestore.loja_api.controllers;

import br.com.gamestore.loja_api.dto.PedidoViewDTO;
import br.com.gamestore.loja_api.model.Pedido;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    //EndPoint para finalizar compra (chekout)
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarCompra(Authentication authentication){
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

            Pedido pedidoSalvo = pedidoService.finalizarCompra(usuarioLogado.getUsername());

            PedidoViewDTO pedidoDTO = new PedidoViewDTO(pedidoSalvo);

            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDTO);
        }catch (ResponseStatusException e){
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getReason());
        }

    }
}
