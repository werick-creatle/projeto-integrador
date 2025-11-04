package br.com.gamestore.loja_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
 * UserDetails: É uma interface do Spring Security.
 * Nossa entidade Usuario agora É um "Detalhe de Usuário" que o Spring Security
 * entende como usar para fazer autenticação e autorização.
 */
@Table(name = "tb_usuario")
@Entity(name = "Usuario") // Corrigido (estava "Usario" com 's' antes)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O 'login' (pode ser o email ou um username)
    @Column(unique = true) // Garante que não teremos dois logins iguais
    private String login;

    // A senha (que será armazenada com hash)
    private String senha;

    // --- A CORREÇÃO MAIS IMPORTANTE ESTÁ AQUI ---
    // Deve ser .STRING para salvar o texto "ADMIN" ou "USER"
    // .ORDINAL salva o número (0 ou 1), o que causa o erro 403
    @Enumerated(EnumType.STRING)
    private UsuarioRole role;


    // --- Métodos obrigatórios da interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define as permissões (roles) do usuário
        if (this.role == UsuarioRole.ADMIN) {
            // Se for ADMIN, ele tem permissão de ADMIN e de USER
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            // Se for USER, ele tem apenas permissão de USER
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha; // O Spring Security pega a senha aqui
    }

    @Override
    public String getUsername() {
        return this.login; // O Spring Security pega o login aqui
    }

    // --- Métodos de status da conta (vamos deixar 'true' por padrão) ---
    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta não expirou
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais (senha) não expiraram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está habilitada
    }
}

