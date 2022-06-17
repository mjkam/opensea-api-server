package com.mjkam.openseaapiserver.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mjkam.openseaapiserver.common.time.ServerTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtTemplateTests {
    private JwtTemplate sut;
    @Mock
    private ServerTime serverTime;
    @Mock
    private JwtProperties jwtProperties;


    @BeforeEach
    void setup() {
        given(jwtProperties.getSecret()).willReturn(jwtSecret("secret"));
        given(jwtProperties.getIssuer()).willReturn(issuer("opensea.io"));

        sut = new JwtTemplate(serverTime, jwtProperties);
    }

    @Test
    @DisplayName("Jwt claim 안에 userId 가 없으면 JWTVerificationException 발생")
    void When_userId_is_not_exist_then_throw_JWTVerificationException() {
        //given
        String jwt = buildJwt(1, null);

        //when //then
        assertThatThrownBy(() -> sut.decodeJwt(jwt))
                .isInstanceOf(JWTVerificationException.class)
                .hasMessage("userId is null");
    }

    @Test
    @DisplayName("만료된 Jwt 를 decode 하면 JWTVerificationException 발생")
    void When_decode_expired_jwt_then_throw_JWTVerificationException() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(1L);
        given(jwtProperties.getDuration()).willReturn(duration(100));
        given(serverTime.getCurrentDateTime()).willReturn(currentDateTime);

        String jwt = sut.createJwt(userId(1L));

        //when then
        assertThatThrownBy(() -> sut.decodeJwt(jwt))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    @DisplayName("jwt 를 decode 하면 userId 를 포함한 JwtClaim 을 리턴")
    void When_decode_jwt_then_return_JwtClaim_which_contains_userId() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        given(jwtProperties.getDuration()).willReturn(duration(100));
        given(serverTime.getCurrentDateTime()).willReturn(currentDateTime);
        String jwt = sut.createJwt(userId(1L));

        //when
        JwtClaim result = sut.decodeJwt(jwt);

        //then
        assertThat(result.getUserId()).isEqualTo(userId(1L));
    }

    @Test
    @DisplayName("생성된 jwt 는 userId 와 properties 정의된 기간만큼 떨어진 만료시간을 가짐")
    void Jwt_contains_userId_And_properties_expire_time() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        given(serverTime.getCurrentDateTime()).willReturn(currentDateTime);
        given(jwtProperties.getDuration()).willReturn(duration(100));

        //when
        String jwt = sut.createJwt(userId(1L));

        //then
        DecodedJWT decodedJWT = decodeJwt(jwt);
        assertThat(decodedJWT.getClaim("userId").asLong()).isEqualTo(userId(1L));
        assertThat(decodedJWT.getIssuedAt()).isEqualTo(Timestamp.valueOf(currentDateTime));
        assertThat(decodedJWT.getExpiresAt()).isEqualTo(Timestamp.valueOf(currentDateTime.plusSeconds(duration(100))));
    }

    private DecodedJWT decodeJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret("secret")); //use more secure key
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer("opensea.io"))
                .build(); //Reusable verifier instance
        return verifier.verify(token);
    }

    private String buildJwt(Integer version, Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusSeconds(1000L)));
        if(version != null) {
            builder = builder.withClaim("version", version);
        }
        if(userId != null) {
            builder = builder.withClaim("userId", userId);
        }
        return builder.sign(algorithm);
    }


    private String jwtSecret(String secret) {
        return secret;
    }

    private int duration(int duration) {
        return duration;
    }

    private String issuer(String issuer) {
        return issuer;
    }

    private long userId(long userId) {
        return userId;
    }
}
