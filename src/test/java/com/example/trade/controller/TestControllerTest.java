package com.example.trade.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.security.jwt.TokenProvider;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(TestController.class)
class TestControllerTest extends ControllerConfig{

    @MockBean
    TokenProvider tokenProvider;

    // json 파일을 수정하기 위해서는 빌드를 다시 해줘야한다.

    @Test
    @DisplayName("테스트1")
    void login() throws Exception {

        UserInfoWithToken info = new UserInfoWithToken();

        //then
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/test1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(document("test-docs",
                        preprocessRequest(prettyPrint()), // request, response를 보여주기 쉽게 그려주는 역할
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("테스트")   // 태그별로 api를 묶을 수 있다.
                                        .description("테스트1") // 해당 API에 대한 설명
                                        .requestFields() // 요청 필드를 입력하는 부분
                                        .responseFields( // 응답 필드를 입력하는 부분
                                                fieldWithPath("status").description("OK"),
                                                fieldWithPath("message").description("message"),
                                                fieldWithPath("data").description("userInfoWithToken"),
                                                fieldWithPath("data.email").description("email")
                                                        .attributes(key("fdf").value("fdsdf")),
                                                fieldWithPath("data.name").description("name"),
                                                fieldWithPath("data.nickname").description("nickname"),
                                                fieldWithPath("data.profile").description("profile"),
                                                fieldWithPath("data.accessToken").description("accessToken")
                                        )
                                        .responseSchema(Schema.schema("API"))
                                        .build()
                        ) )
                );

        resultActions.andExpect(status().isOk());


    }

//    ,resource(ResourceSnippetParameters.builder()
//                                .description("테스트")
//                                .requestFields()
//                                .responseFields(
//            fieldWithPath("status").description("상태"),
//    fieldWithPath("message").description("상태"),
//    fieldWithPath("data").description("데이터")
//                                ).build()

}