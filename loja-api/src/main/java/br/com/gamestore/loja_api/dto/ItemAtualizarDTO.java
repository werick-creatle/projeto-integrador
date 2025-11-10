package br.com.gamestore.loja_api.dto;

import jakarta.validation.constraints.Min;

public record ItemAtualizarDTO(@Min(value = 1, message = "A quantidade deve ser pelo menos 1") int quantidade){}
