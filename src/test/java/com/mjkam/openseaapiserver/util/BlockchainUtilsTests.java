package com.mjkam.openseaapiserver.util;

import com.mjkam.openseaapiserver.util.BlockchainUtils;
import com.mjkam.openseaapiserver.util.VerifySignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockchainUtilsTests {

    @Test
    @DisplayName("잘못된 signature 형식은 VerifySignatureException 발생")
    void Given_wrong_signature_then_throw_VerifySignatureException() {
        String signer = "0x4bb8744e83977859a9670ea08d3e87b00dec33ac";
        String message = "Hello World";
        String signature = "0x123abc123";

        //when
        assertThatThrownBy(() -> BlockchainUtils.isRightSigner(signer, message, signature))
                .isInstanceOf(VerifySignatureException.class);
    }

    @Test
    @DisplayName("message 와 signature 가 안맞으면 false 리턴")
    void When_message_and_signature_not_match_then_return_false() {
        String signer = "0x4bb8744e83977859a9670ea08d3e87b00dec33ac";
        String message = "Hello World1";
        String signature = "0xb9c41a9d2e0e54c6a19f475e817147e86721ca2660536f9a8830976f279444c4691e5d4927de12ec62a0ccc4acf65a3689a3265cfcc64f960576e7ee2b087a121b";

        //when
        boolean result = BlockchainUtils.isRightSigner(signer, message, signature);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("signature 와 message 를 사용하면 signer 가 누구인지 확인가능")
    void Given_signature_and_message_then_can_verify_signer() {
        String signer = "0x4bb8744e83977859a9670ea08d3e87b00dec33ac";
        String message = "Hello World";
        String signature = "0xb9c41a9d2e0e54c6a19f475e817147e86721ca2660536f9a8830976f279444c4691e5d4927de12ec62a0ccc4acf65a3689a3265cfcc64f960576e7ee2b087a121b";

        //when
        boolean result = BlockchainUtils.isRightSigner(signer, message, signature);

        //then
        assertThat(result).isTrue();
    }

}
