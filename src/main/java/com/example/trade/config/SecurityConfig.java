package com.example.trade.config;

import com.example.trade.security.jwt.JwtAccessDeniedHandler;
import com.example.trade.security.jwt.JwtAuthenticationEntryPoint;
import com.example.trade.security.jwt.JwtAuthenticationFilter;
import com.example.trade.security.oauth2.CustomOAuth2UserService;
import com.example.trade.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        return http
//                .userDetailsService(customUserDetailsService)
//                .passwordManagement(pas -> passwordEncoder())
                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                        .requestMatchers("/api/auth/**","oauth2/**","/","/ws/**","/chat","/chatlist","/favicon.ico").permitAll()
                                        .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(excep -> excep
                                        .accessDeniedHandler(jwtAccessDeniedHandler)
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                                .oauth2Login(oauth-> oauth
                                        .userInfoEndpoint(userInfo->userInfo.userService(customOAuth2UserService))
                                        .defaultSuccessUrl("/api/auth/social"))
                .build();
    }
}