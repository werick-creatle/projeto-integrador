/*
 * Essa classe (na verdade, enum) UsuarioRole representa as funções (roles) que um usuário pode ter.
 * Ex: ADMIN = administrador, USER = usuário comum.
 */

package br.com.gamestore.loja_api.model;

public enum UsuarioRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}