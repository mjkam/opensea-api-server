package com.mjkam.openseaapiserver.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mjkam.openseaapiserver.common.time.ServerTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class JwtTemplate {
    private final ServerTime serverTime;
    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Autowired
    public JwtTemplate(ServerTime serverTime, JwtProperties jwtProperties) {
        this.serverTime = serverTime;
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.verifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
    }

    public String createJwt(long userId) {
        LocalDateTime issueTime = serverTime.getCurrentDateTime();
        LocalDateTime expireTime = issueTime.plusSeconds(jwtProperties.getDuration());

        return JWT.create()
                .withIssuedAt(Timestamp.valueOf(issueTime))
                .withExpiresAt(Timestamp.valueOf(expireTime))
                .withIssuer(jwtProperties.getIssuer())
                .withClaim("userId", userId)
                .sign(algorithm);
    }

    public JwtClaim decodeJwt(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);

        Long userId = decodedJWT.getClaim("userId").asLong();
        if(userId == null) {
            throw new JWTVerificationException("userId is null");
        }

        return new JwtClaim(userId);
    }
}
