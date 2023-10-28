package com.example.trade.security.jwt;

import com.example.trade.domain.UserAccount;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider{

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.token-in-second}")
    private String tokenExpiredTime;

    private static final String AUTHORITIES_KEY = "auth";
    private Key key;

    public String createToken(UserAccount user){

        // 권한 가져오기
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 만료시간 설정하기
        long now = (new Date()).getTime();
        Date expiredAt = new Date(now + Integer.parseInt(tokenExpiredTime));

        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredAt)
                .compact();

    }

    public String createRefreshToken(UserAccount user){

        // 현재 날짜 가져오기
        Calendar now = Calendar.getInstance();

        // 현재 날짜에서 7일 더하기
        now.add(Calendar.DAY_OF_MONTH, 7);

        // Calendar 객체를 Date 객체로 변환
        Date expiredAt = now.getTime();

        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredAt)
                .compact();

    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends  GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        String userId = claims.getSubject();

        return new UsernamePasswordAuthenticationToken(userId, null, authorities);

    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {

            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {

            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {

            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
