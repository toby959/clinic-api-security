package com.toby959.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.toby959.api.domain.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generateToken(User user) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
          return JWT.create()
                    .withIssuer("clinic-api-security")       // name API
                    .withSubject(user.getLogin())            // id User
                    .withClaim("id", user.getId())     // name User
                    .withExpiresAt(generateExpirationDate()) // Time expiration
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al crear el token JWT !!!", exception);
        }
    }

    private Instant generateExpirationDate() {
//        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));

        return LocalDateTime.now().plusMonths(1) // + months
                .toInstant(ZoneOffset.of("-03:00"));
    }

public String getSubject(String token) {
    try {
        Algorithm algorithm = Algorithm.HMAC256(apiSecret);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer("clinic-api-security")
                .build()
                .verify(token);

        // return user directly
        return decodedJWT.getSubject();
    } catch (JWTVerificationException exception) {
        throw new RuntimeException("Token inválido: " + exception.getMessage(), exception);
    }
  }
}
