package br.com.gamestore.loja_api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record JogoCadastroDTO(

        @NotBlank(message = "O nome não pode estar em branco!")
        @Size(min = 3, max = 100,message = "O nome deve ter entre 3 e 100 caracteres! ")
        String nome,

        @NotBlank(message = "A URL da imagem não pode estar em branco!")
        String urlImagemCapa,

        @NotNull(message = "O campo preço não pode estar em branco!")
        @Positive(message = "O preço deve ser um valor positivo!")
        BigDecimal preco,

        @NotBlank(message = "O campo plataforma não pode estar vazio!")
        String plataforma,

        @NotNull(message = "O campo data de lançamento não pode ser nula!")
        @PastOrPresent(message = "A data de lançamento não pode ser no futuro!")
        LocalDate dataLancamento,

        @NotBlank(message = "O campo descrição não pode estar em branco!")
        String descricao,

        @NotBlank(message = "O campo gênero não pode estar em branco!")
        String genero,

        @Min(value = 0, message = "O estoque não pode ser negativo")
        int quantidadeEstoque

) {}