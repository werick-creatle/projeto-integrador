package br.com.gamestore.loja_api.controllers;

import br.com.gamestore.loja_api.dto.CarrinhoViewDTO;
import org.springframework.security.core.Authentication;
import br.com.gamestore.loja_api.dto.ItemAdicionarDTO;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.services.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import br.com.gamestore.loja_api.dto.ItemAtualizarDTO;


@RestController
@RequestMapping("/api/carrinho")
//@CrossOrigin("*")
public class CarrinhoController {

    @Autowired
    CarrinhoService carrinhoService;

    @PutMapping("/atualizar/{itemId}")
    public ResponseEntity<?> atualizarQuantidade(@PathVariable Long itemId, @Valid @RequestBody ItemAtualizarDTO dados, Authentication authentication){ //Essa anotação @PathVariable pega um valor q veio dentro de uma URL e joga dentro do metodo como uma variavel
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            carrinhoService.atualizarQuantidadeItem(itemId,dados ,usuarioLogado);

            return ResponseEntity.ok().build();
        }catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }




    @DeleteMapping("/remover/{itemId}")
    public ResponseEntity<?> removerDoCarrinho(@PathVariable Long itemId, Authentication authentication) {
        try {
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

            carrinhoService.removerDoCarrinho(itemId, usuarioLogado);

            //Rertorna o status 204 (Sucesso sem corpo)
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }


    @PostMapping("/adicionar")
    public ResponseEntity<Void> adicionarAoCarrinho(@Valid @RequestBody ItemAdicionarDTO dados, Authentication authentication) { //Esse authentication pega o cracha do usuario logado, pois ele só pode adicionar intes ao carrinho se ele estiver logado
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        carrinhoService.adicionarAoCarrinho(dados, usuarioLogado);
        return ResponseEntity.ok().build();
    }

     //Endpoint para visualizar o conteúdo do carrinho do usuário logado.
     //Protegido pela regra /api/carrinho/** no SecurityConfig.
    @GetMapping
    public ResponseEntity<CarrinhoViewDTO> verCarrinho(Authentication authentication) {
        //Pega o usuario logado (dono do cracha )
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        //Chama o Gerente (Sevice ) para buscar e formatar o carrinho
        CarrinhoViewDTO carrinhoDTO = carrinhoService.verCarrinho(usuarioLogado.getUsername());

        //Retorna 200 ok com o carrinho (em formato DTO) no corpo
        return ResponseEntity.ok(carrinhoDTO);
    }
}
