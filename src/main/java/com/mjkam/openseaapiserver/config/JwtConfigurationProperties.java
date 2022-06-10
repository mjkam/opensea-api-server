package com.mjkam.openseaapiserver.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.validation.constraints.NotNull;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "opensea.jwt")
@Getter
public class JwtConfigurationProperties {
    @NotNull
    private String secret;
    @NotNull
    private Integer duration;
}
