package com.mjkam.openseaapiserver.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtClaim {
    private Long userId;

    public JwtClaim(long userId) {
        this.userId = userId;
    }
}
