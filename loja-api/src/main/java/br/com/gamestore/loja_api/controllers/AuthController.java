// Este Controller é responsável por cuidar das rotas de autenticação (Registro e Login).

package br.com.gamestore.loja_api.controllers;

import java.time.LocalDate;

import br.com.gamestore.loja_api.dto.LoginDTO;
import br.com.gamestore.loja_api.dto.RegistroDTO;
import br.com.gamestore.loja_api.dto.TokenDTO;
import br.com.gamestore.loja_api.model.Carrinho;
import br.com.gamestore.loja_api.model.Usuario;
import br.com.gamestore.loja_api.model.UsuarioRole;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import br.com.gamestore.loja_api.services.TokenService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/auth") // Todas as rotas aqui começarão com /login
//@CrossOrigin("*")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Aqui injetei o Bean PasswordEncoder que foi criado no SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint de REGISTRO (Cadastro de novo usuário)
    // Rota: POST http://localhost:8080/login/register
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroDTO dados) {

        // 1. Verifica se o login (email/username) já está em uso
        // (Usando o método 'findByLogin' que criamos no UsuarioRepository)
        if (this.usuarioRepository.findByLogin(dados.login()) != null) {
            // Se já existir, retorna um erro "Bad Request" (400)
            return ResponseEntity.badRequest().body("Este login já está em uso.");
        }

        // Se o login estiver livre, criptografar a senha
        // Usamos o passwordEncoder que eu configurei
        String senhaCriptografada = this.passwordEncoder.encode(dados.senha());

        // Criar o novo objeto Usuario
        // Por padrão, todo novo registro é um usuário comum (ROLE_USER)
        Usuario novoUsuario = new Usuario(
                null,
                dados.login(),
                senhaCriptografada,
                UsuarioRole.USER,
                dados.nomeCompleto(),
                dados.dataNascimento(),
                null
        );

        //aqui estou criando um novo carrinho e pasando o novoUsuario para ele
        Carrinho novoCarrinho = new Carrinho(novoUsuario);

        novoUsuario.setCarrinho(novoCarrinho);

        // Salvar o novo usuário no banco de dados
        this.usuarioRepository.save(novoUsuario);

        // Retornar uma resposta de sucesso (201 Created)
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
    }

    // Endpoint de LOGIN (Autenticação)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dados) {
        try {
            // 3. Cria um "pacote" com o login e senha (ainda não autenticado)
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    dados.login(),
                    dados.senha()
            );

            var auth = this.authenticationManager.authenticate(usernamePassword);
            var usuario = (Usuario) auth.getPrincipal();
            var token = this.tokenService.gerarToken(usuario);

            return ResponseEntity.ok(new TokenDTO(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login ou senha inválidos.");
        }
    }
}
