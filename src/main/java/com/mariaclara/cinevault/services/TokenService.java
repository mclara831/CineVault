package com.mariaclara.cinevault.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mariaclara.cinevault.entities.UserRole;
import com.mariaclara.cinevault.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expirationTime}")
    private long secondsToAdd;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var now = Instant.now();
        var expirationTime = now.plusSeconds(secondsToAdd);

        var scope = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.joining(" "));

        var token = JWT.create()
                .withIssuer("cineVault")
                .withSubject(user.getId().toString())
                .withClaim("scope", scope)
                .withIssuedAt(now)
                .withExpiresAt(expirationTime)
                .sign(algorithm);
        return token;
    }
}
