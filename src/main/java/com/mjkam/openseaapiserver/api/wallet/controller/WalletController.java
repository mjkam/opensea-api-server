package com.mjkam.openseaapiserver.api.wallet.controller;

import com.mjkam.openseaapiserver.api.wallet.controller.dto.ConnectWalletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController {
    @PostMapping("/wallet/connect")
    public ResponseEntity connectWallet(@RequestBody @Validated ConnectWalletRequest request) {
        System.out.println(request.getWalletAddress());
        return null;
    }
}
