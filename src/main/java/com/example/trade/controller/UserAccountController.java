package com.example.trade.controller;


import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.Api;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.dto.response.UserInfoWithTokens;
import com.example.trade.service.UserAccountService;
import com.example.trade.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final HttpSession httpSession;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request
    , HttpServletResponse response){

        UserAccount user = userAccountService.login(request);
        UserInfoWithTokens userInfoWithTokens = userAccountService.generateTokens(user);

        UserInfoWithToken userInfo = UserInfoWithToken.from(userInfoWithTokens);

        cookieUtil.addCookie(response,"refreshToken", userInfoWithTokens.getRefreshToken());

        log.info("{}님이 로그인하였습니다.",userInfo.getName());

        return ResponseEntity.ok()
                .body(Api.builder()
                        .status("OK")
                        .message("로그인에 성공했습니다.")
                        .data(userInfo));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){

        UserAccount user = userAccountService.singUp(request);
        log.info("{}님이 회원가입하였습니다.",user.getName());

        return ResponseEntity.status(201)
                .body(Api.builder()
                        .status("Created")
                        .message("회원가입이 성공했습니다.")
                        .data(null));
    }

    @GetMapping("/social")
    public ResponseEntity<?> socialLogin(HttpServletResponse response){
        UserAccount user = (UserAccount)httpSession.getAttribute("user");

        UserInfoWithTokens userInfoWithTokens = userAccountService.generateTokens(user);
        UserInfoWithToken userInfo = UserInfoWithToken.from(userInfoWithTokens);

        cookieUtil.addCookie(response,"refreshToken", userInfoWithTokens.getRefreshToken());

        log.info("{}님이 소셜로그인 하였습니다.",userInfo.getName());

        return ResponseEntity.ok()
                .body(Api.builder()
                        .status("OK")
                        .message("소셜로그인에 성공했습니다.")
                        .data(userInfo));
    }

    @GetMapping("/test")
    public String test(){
        return "하이";
    }


}
