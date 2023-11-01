package com.example.trade.service;

import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.repository.UserAccountRepository;
import com.example.trade.security.jwt.TokenProvider;
import com.example.trade.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("Auth 테스트")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock private UserAccountRepository userAccountRepository;
    @Mock private TokenProvider tokenProvider;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private CookieUtil cookieUtil;
    @InjectMocks private UserAccountService userAccountService;

    @DisplayName("회원가입 테스트")
    @Test
    void givenSignUpRequest_whenSignUp_then_returnUserAccount(){
        // Given
        SignUpRequest request = new SignUpRequest("pyjhoop@naver.com","qwe123","qwe","qwe","/기본프로필 사진 경로");
        UserAccount user = new UserAccount();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setRole("ROLE_USER");
        user.setProfile("/기본프로필 사진 경로");

        given(passwordEncoder.encode(request.getPassword())).willReturn("qwe1231");
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(user);

        // When
        UserAccount result = userAccountService.singUp(request);

        // Then
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("email",request.getEmail())
                .hasFieldOrPropertyWithValue("name",request.getName())
                .hasFieldOrPropertyWithValue("nickname",request.getNickname());

    }


}