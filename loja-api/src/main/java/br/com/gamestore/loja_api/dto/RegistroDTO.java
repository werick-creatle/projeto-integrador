package br.com.gamestore.loja_api.dto;

/*
 * Este é um 'record'. É uma classe especial do Java moderno que serve só para guardar dados.
 *
 * Ao declarar assim, o Java automaticamente cria os métodos .login() e .senha().
 */
public record RegistroDTO(String login, String senha) {


}