package br.com.gamestore.loja_api.security;

import br.com.gamestore.loja_api.config.SecurityConfig;
import br.com.gamestore.loja_api.repositories.UsuarioRepository;
import br.com.gamestore.loja_api.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Este é o método principal do "guarda". Tudo que chega na API passa por aqui primeiro.
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recuperarToken(request);


        if (token != null) {
            var login = tokenService.validarToken(token);
            UserDetails usuario = usuarioRepository.findByLogin(login);

            if (usuario != null){
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request,response );
    }

    //Classe q verifica o token e retrna ele
    private String recuperarToken(HttpServletRequest request) {
        var tokenAutenticacao = request.getHeader("Authorization");
        if (tokenAutenticacao == null) {
            return null;
        }
        return tokenAutenticacao.replace("Bearer ", "");
    }
}

