package com.mjkam.openseaapiserver.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;


@Configuration
@ConfigurationProperties(prefix = "server.jwt")
@Getter
@Setter
public class JwtProperties {
    @NotEmpty
    private String secret;
    @NotEmpty
    private Integer duration;
    @NotEmpty
    private String issuer;
}
