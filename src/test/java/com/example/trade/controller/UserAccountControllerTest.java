package com.example.trade.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.UserInfoWithTokens;
import com.example.trade.security.jwt.TokenProvider;
import com.example.trade.service.UserAccountService;
import com.example.trade.util.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest extends ControllerConfig{

    @MockBean
    TokenProvider tokenProvider;
    @MockBean
    UserAccountService userAccountService;
    @MockBean
    CookieUtil cookieUtil;

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception {
        //given
        LoginRequest request = new LoginRequest("user01@email.com","password");
        UserInfoWithTokens userInfoWithTokens = new UserInfoWithTokens("email","name","nickname","profile","accessToken","refreshToken");
        UserAccount user = new UserAccount();

        given(userAccountService.login(any(LoginRequest.class))).willReturn(user);
        given(userAccountService.generateTokens(any(UserAccount.class))).willReturn(userInfoWithTokens);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(document("login-docs",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("UserAccount")
                                        .description("유저 로그인")
                                        .requestFields(
                                                fieldWithPath("email").description("이메일 주소"),
                                                fieldWithPath("password").description("비밀번호")
                                        ).requestSchema(Schema.schema("LoginRequest"))
                                        .responseFields(
                                                fieldWithPath("status").description("요청 상태"),
                                                fieldWithPath("message").description("메세지"),
                                                fieldWithPath("data").description("유저정보")
                                                        .attributes(key("sffd").value("sadfdfas")),
                                                fieldWithPath("data.email").description("이메일"),
                                                fieldWithPath("data.name").description("이름"),
                                                fieldWithPath("data.nickname").description("닉네임"),
                                                fieldWithPath("data.profile").description("프로필 경로"),
                                                fieldWithPath("data.accessToken").description("접근 토큰")
                                        ).responseSchema(Schema.schema("Login Response"))
                                        .build()
                        )));
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("email","password","name","nickname","profile");
        UserInfoWithTokens userInfoWithTokens = new UserInfoWithTokens("email","name","nickname","profile","accessToken","refreshToken");
        UserAccount user = new UserAccount();

        given(userAccountService.singUp(any(SignUpRequest.class))).willReturn(user);
        given(userAccountService.generateTokens(any(UserAccount.class))).willReturn(userInfoWithTokens);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(document("signUp-docs",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("UserAccount")
                                        .description("유저 회원가입")
                                        .requestFields(
                                                fieldWithPath("email").description("이메일 주소"),
                                                fieldWithPath("password").description("비밀번호"),
                                                fieldWithPath("name").description("이름"),
                                                fieldWithPath("nickname").description("닉네임"),
                                                fieldWithPath("profile").description("프로필 주소")
                                        ).requestSchema(Schema.schema("SignUpRequest"))
                                        .responseFields(
                                                fieldWithPath("status").description("요청 상태"),
                                                fieldWithPath("message").description("메세지"),
                                                fieldWithPath("data").description("null")
                                        ).responseSchema(Schema.schema("SignUp Response"))
                                        .build()
                        )));
        resultActions.andExpect(status().is(201));
    }


}