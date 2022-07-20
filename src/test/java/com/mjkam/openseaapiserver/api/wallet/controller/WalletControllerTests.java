package com.mjkam.openseaapiserver.api.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mjkam.openseaapiserver.api.wallet.controller.dto.ConnectWalletRequest;
import com.mjkam.openseaapiserver.exception.AppError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.mjkam.openseaapiserver.TestHelper.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WalletControllerTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /*
    1. walletAddress 를 보내면 jwt, profileUrl 리턴

    1. walletAddress 가 누락되면 BAD_REQUEST 인데.. code, message INVALID_REQUEST
    2. 
     */

    @Test
    @DisplayName("walletAddress 가 주어지면 유저 데이터를 리턴함")
    void test1() throws Exception {
        //given
        //when then
        mockMvc.perform(post("/wallet/connect")
                        .content(connectWalletRequest(walletAddress("abc")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileUrl").value(""));
    }

    @Test
    @DisplayName("요청 데이터 누락하면 BAD_REQUEST 리턴")
    void When_missing_request_data_then_return_BAD_REQUEST() throws Exception {
        //given
        //when then
        mockMvc.perform(post("/wallet/connect"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(AppError.INVALID_REQUEST.getCode()));
    }
    
    private static String connectWalletRequest(String walletAddress) throws JsonProcessingException {
        ConnectWalletRequest request = new ConnectWalletRequest();
        ReflectionTestUtils.setField(request, "walletAddress", walletAddress);
        return objectMapper.writeValueAsString(request);
    }
    
    private static String walletAddress(String s) { return s; }
}
