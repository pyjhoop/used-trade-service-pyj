package com.example.trade.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("여기 성공1");
        if(!noFilter(request)){

            log.info("여기 성공 2");
            String accessToken = parseToken(request);

            if(accessToken != null && tokenProvider.validateToken(accessToken)){

                log.info("여기 성공3");
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                log.debug("유효한 JWT 토큰이 없습니다");
                throw new AuthenticationServiceException("Invalid or missing token");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    //특정 URI 필터 동작 막기
    private boolean noFilter(HttpServletRequest request){
        log.info(request.getRequestURI());
        return request.getRequestURI().equals("/api/auth")
                || request.getRequestURI().equals("/api/signup")
                || request.getRequestURI().equals("/api/social")
                || request.getRequestURI().equals("/oauth2/authorization/google")
                || request.getRequestURI().equals("/favicon.ico")
                || request.getRequestURI().matches("/oauth2")
                || request.getRequestURI().equals("/");
    }
}