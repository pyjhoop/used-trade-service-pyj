package com.example.trade.service;

import com.example.trade.domain.UserAccount;
import com.example.trade.dto.request.LoginRequest;
import com.example.trade.dto.request.SignUpRequest;
import com.example.trade.dto.response.UserInfoWithToken;
import com.example.trade.repository.UserAccountRepository;
import com.example.trade.security.jwt.TokenProvider;
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


    public UserAccount singUp(SignUpRequest request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setProfile("/기본프로필 사진 경로");
        return userAccountRepository.save(request.toEntity());
    }

    public UserInfoWithToken login(LoginRequest request, HttpServletResponse response){

        UserAccount user = userAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()+"에 해당하는 유저를 찾을 수 없습니다."));



        if(passwordEncoder.matches(request.getPassword(),user.getPassword())){

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();


            String accessToken = tokenProvider.createToken(user);
            UserInfoWithToken userInfoWithToken = UserInfoWithToken.from(user);
            userInfoWithToken.setAccessToken(accessToken);

            String refreshToken = tokenProvider.createRefreshToken(user);

            Duration expiration = Duration.ofDays(7);
            valueOperations.set("refresh"+user.getId(),refreshToken,expiration);

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*7);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

            return userInfoWithToken;
        }else{
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }
    }

    public UserInfoWithToken login(UserAccount user, HttpServletResponse response){

        String accessToken = tokenProvider.createToken(user);
        UserInfoWithToken userInfoWithToken = UserInfoWithToken.from(user);
        userInfoWithToken.setAccessToken(accessToken);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = tokenProvider.createRefreshToken(user);

        Duration expiration = Duration.ofDays(7);
        valueOperations.set("refresh"+user.getId(),refreshToken,expiration);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*7);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return userInfoWithToken;
    }


}
