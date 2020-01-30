package com.cloud.cmr.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException {
        User user = (User) authentication.getPrincipal();
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        String token = Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", jwtProperties.getType())
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenDuration()))
                .compact();

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
