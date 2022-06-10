package com.mjkam.openseaapiserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mjkam.openseaapiserver.common.TimeService;
import com.mjkam.openseaapiserver.config.JwtConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final TimeService timeService;
    private final JwtConfigurationProperties jwtConfigurationProperties;
    public String createJwt(long userId) {
        LocalDateTime currentDateTime = timeService.getCurrentDateTime();
        LocalDateTime expireDateTime = currentDateTime.plusSeconds(jwtConfigurationProperties.getDuration());

        Algorithm algorithm = Algorithm.HMAC256(jwtConfigurationProperties.getSecret());
        return JWT.create()
                .withIssuedAt(Timestamp.valueOf(currentDateTime))
                .withExpiresAt(Timestamp.valueOf(expireDateTime))
                .withClaim("userId", userId)
                .sign(algorithm);
    }

    public long getUserIdFromJwt(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256("secret"); //use more secure key
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getClaim("userId").asLong();
    }
}
