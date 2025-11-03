/*
 * Esta classe é a nossa "Fábrica de Tokens".
 * Ela é responsável por gerar o "crachá" (Token JWT)
 * que o usuário vai receber após o login.
 */

package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    //Pega a "senha secreta" que definimos no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    //Define quem é o "emissor" (issuer) do token
    private String issuer = "GameStore API";

    /**
     * Método principal que GERA um novo Token JWT para um usuário.
     */
    public String gerarToken(Usuario usuario) {
        try {
            //Define o algoritmo de assinatura (HMAC256) usando a nossa senha secreta
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer(issuer) // Define quem emitiu o token
                    .withSubject(usuario.getUsername()) // Define de quem é o token (o "assunto")
                    .withExpiresAt(gerarDataDeExpiracao()) // Define quando o token expira
                    // .withClaim("role", usuario.getRole().toString()) // (Opcional) Adiciona a permissão no token
                    .sign(algoritmo); // 4. Assina o token com o algoritmo e a senha

            return token;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o Token JWT", exception);
        }
    }

    /**
     * Método privado que define a data de expiração do token.
     * (Ex: 2 horas a partir de agora).
     */
    private Instant gerarDataDeExpiracao() {
        return LocalDateTime.now()
                .plusHours(2) // O token vai expirar em 2 horas
                .toInstant(ZoneOffset.of("-03:00")); // Define o fuso horário (ex: Brasília)
    }
}

