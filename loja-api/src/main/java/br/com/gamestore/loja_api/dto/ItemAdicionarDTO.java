package br.com.gamestore.loja_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record ItemAdicionarDTO(

        @NotNull(message = "O ID do jogo não pode ser nulo.")
        Long jogoId,

        @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
        int quantidade
) {
}

