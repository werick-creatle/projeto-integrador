package br.com.gamestore.loja_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record JogoCadastroDTO(
        String nome,
        String urlImagemCapa,
        BigDecimal preco,
        String plataforma,
        LocalDate dataLancamento,
        String descricao,
        String genero
) {}