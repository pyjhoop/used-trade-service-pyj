package com.example.trade.controller;

import com.example.trade.dto.response.Api;
import com.example.trade.dto.response.UserInfoWithToken;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test1")
    public Api<UserInfoWithToken> test1(){
        return Api.<UserInfoWithToken>builder()
                .status("OK")
                .message("good1")
                .data(new UserInfoWithToken())
                .build();
    }
}
