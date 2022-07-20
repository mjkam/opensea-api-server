package com.mjkam.openseaapiserver.api.wallet.controller.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class ConnectWalletRequest {
    @NotNull
    @NotEmpty
    private String walletAddress;
}
