package com.example.trade.service;

import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.repository.UserAccountRepository;
import com.example.trade.security.jwt.TokenProvider;
import com.example.trade.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final CookieUtil cookieUtil;

    public UserAccount singUp(SignUpRequest request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setProfile("/기본프로필 사진 경로");
        return userAccountRepository.save(request.toEntity());
    }

    /**
     * 일반로그인 메서드
     */
    public UserInfoWithToken login(LoginRequest request, HttpServletResponse response){

        UserAccount user = userAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()+"에 해당하는 유저를 찾을 수 없습니다."));

        if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
            // 토큰 생성 및 refreshToekn 쿠키 설정
            return generateTokens(user, response);
        }else{
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }
    }

    /**
     * 소셜로그인 메서드
     */
    public UserInfoWithToken login(UserAccount user, HttpServletResponse response){

        return generateTokens(user, response);
    }


    /**
     * accessToken 과 refreshToken 생성후 refreshToken은 쿠키에 설정
     */
    public UserInfoWithToken generateTokens(UserAccount user, HttpServletResponse response){

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String accessToken = tokenProvider.createToken(user);
        UserInfoWithToken userInfoWithToken = UserInfoWithToken.from(user);
        userInfoWithToken.setAccessToken(accessToken);

        String refreshToken = tokenProvider.createRefreshToken(user);

        Duration expiration = Duration.ofDays(7);
        valueOperations.set("refresh"+user.getId(),refreshToken,expiration);
        cookieUtil.addCookie(response,"refreshToken", refreshToken);

        return  userInfoWithToken;
    }


}
