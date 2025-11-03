package br.com.gamestore.loja_api.model;

import jakarta.annotation.Nullable;
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


@Table(name = "tb_usuario")
//Diz ao JPA (Java Persistence API) que essa classe está mapeada para uma tabela no banco de dados chamada tb_usuario
@Entity(name = "Usuario") //nforma que essa classe é uma entidade JPA, ou seja, ela representa uma tabela no banco.
//@Getter //cria automaticamente todos os métodos getters
//@NoArgsConstructor //Cria automaticamente um construtor sem argumentos
//@AllArgsConstructor //Cria um construtor com todos os campos da classe como parâmetros.
public class Usuario implements UserDetails {// indica que a classe representa um usuário do sistema e fornece dados de autenticação pro Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    //O loguin, pode ser o email ou o username
    @Column(unique = true)//garante q não tenha dois logins iguais
    private String login;

    // A senha (que será armazenada com hash)
    private String senha;

    // faz o JPA salvar o valor do enum como texto (ex: "ADMIN", "USER") em vez de número.
    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

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

    /**
     * Ele retorna uma lista de objetos GrantedAuthority, que representam as roles (funções) do usuário — como ROLE_USER ou ROLE_ADMIN.
     * Essas roles são usadas pelo Spring Security pra decidir se o usuário pode acessar determinada rota, método ou recurso.
     */
    @Override
    public boolean isEnabled() {
        return true; // A conta está habilitada
    }

    public Usuario() {
    }

    public Usuario(@Nullable Long id, String login, String senha, UsuarioRole role) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.role = role;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public UsuarioRole getRole() {
        return role;
    }
}
