package com.insexba.relacionamento_insex.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.insexba.relacionamento_insex.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("App_Relacionamento_INSEX")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .withClaim("id", user.getId())  // Claim personalizada: ID do usuário
                    .withClaim("role", (user.getTypeUser()).toString())  // Claim personalizada: Tipo de usuário
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            // Log da exceção para diagnóstico
            System.err.println("Erro ao criar o token: " + exception.getMessage());
            throw new RuntimeException("Error while authenticating");
        }
    }


    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("App_Relacionamento_INSEX")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
