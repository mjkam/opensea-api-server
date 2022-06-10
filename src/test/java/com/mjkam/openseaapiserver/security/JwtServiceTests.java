package com.mjkam.openseaapiserver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mjkam.openseaapiserver.common.TimeService;
import com.mjkam.openseaapiserver.config.JwtConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {
    private JwtService sut;
    @Mock
    private TimeService timeService;
    @Mock
    private JwtConfigurationProperties jwtConfigurationProperties;

    @BeforeEach
    void setup() {
        given(jwtConfigurationProperties.getSecret()).willReturn(secret("secret"));
        given(jwtConfigurationProperties.getDuration()).willReturn(duration(100));

        sut = new JwtService(timeService, jwtConfigurationProperties);
    }

    @Test
    @DisplayName("만료된 jwt 입력하면 JWTVerificationException 발생")
    void Given_ExpiredJwt_When_GetUserIdFromJwt_ThenThrowJWTVerificationException() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(10L);
        given(timeService.getCurrentDateTime()).willReturn(currentDateTime);
        String jwt = sut.createJwt(userId(1L));

        //when then
        assertThatThrownBy(() -> sut.getUserIdFromJwt(jwt))
                .isInstanceOf(JWTVerificationException.class);

    }

    @Test
    @DisplayName("jwt 입력하면 userId 리턴")
    void Given_NotExpiredJwt_When_GetUserIdFromJwt_ThenReturn() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        given(timeService.getCurrentDateTime()).willReturn(currentDateTime);

        String jwt = sut.createJwt(userId(1L));

        //when
        long resultUserId = sut.getUserIdFromJwt(jwt);

        //then
        assertThat(resultUserId).isEqualTo(userId(1L));
    }


    @Test
    @DisplayName("userId 입력하면 Jwt 토큰 리턴")
    void Given_CurrentDateTime_When_CreateJwt_Then_ReturnNotExpiredJwt() {
        //given
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        given(timeService.getCurrentDateTime()).willReturn(currentDateTime);

        //when
        String resultJwt = sut.createJwt(userId(1L));

        //then
        DecodedJWT decodedJWT = decodeJwt(resultJwt);
        assertThat(decodedJWT.getClaim("userId").asLong()).isEqualTo(userId(1L));
        assertThat(decodedJWT.getIssuedAt()).isEqualTo(Timestamp.valueOf(currentDateTime));
        assertThat(decodedJWT.getExpiresAt()).isEqualTo(Timestamp.valueOf(currentDateTime.plusSeconds(duration(100))));

    }

    private DecodedJWT decodeJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret("secret")); //use more secure key
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        return verifier.verify(token);
    }

    private long userId(long userId) { return userId; }
    private String secret(String secret) { return secret; }
    private int duration(int duration) { return duration; }
}
