package br.com.gamestore.loja_api.services;

import br.com.gamestore.loja_api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hora

            return JWT.create()
                    .withSubject(usuario.getLogin())
                    .withClaim("role", usuario.getRole().name()) // ✅ Role incluída
                    .withExpiresAt(expiration)
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public DecodedJWT validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token); // ✅ Retorna token completo para ler claims
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}