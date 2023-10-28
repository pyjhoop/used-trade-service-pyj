package com.example.trade.controller;


import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.service.UserAccountService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final HttpSession httpSession;

    @PostMapping("/auth")
    public ResponseEntity login(@RequestBody LoginRequest request
    , HttpServletResponse response){

        UserInfoWithToken userInfo = userAccountService.login(request, response);

        return ResponseEntity.ok()
                .body(userInfo);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUpRequest request){

        UserAccount user = userAccountService.singUp(request);

        return ResponseEntity.ok()
                .body(user);

    }

    @GetMapping("/social")
    public UserInfoWithToken socialLogin(HttpServletResponse response){
        UserAccount user = (UserAccount)httpSession.getAttribute("user");

        UserInfoWithToken userInfo = userAccountService.login(user, response);
        return userInfo;
    }

    @GetMapping("/test")
    public String test(){

        return "tet";
    }

}
