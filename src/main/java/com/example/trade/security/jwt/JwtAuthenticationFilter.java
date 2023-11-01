package com.example.trade.security.jwt;

import io.jsonwebtoken.*;
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
import java.security.Key;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private Key key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(!noFilter(request)){

            String accessToken = parseToken(request);

            if(accessToken != null && tokenProvider.validateToken(accessToken, request)){

                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
        return request.getRequestURI().equals("/api/auth/login")
                || request.getRequestURI().equals("/api/auth/signup")
                || request.getRequestURI().equals("/api/auth/social")
                || request.getRequestURI().equals("/oauth2/authorization/google")
                || request.getRequestURI().equals("/favicon.ico")
                || request.getRequestURI().matches("/oauth2")
                || request.getRequestURI().equals("/");
    }

}