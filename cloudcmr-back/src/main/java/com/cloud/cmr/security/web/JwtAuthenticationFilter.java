package com.cloud.cmr.security.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private ObjectMapper mapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, ObjectMapper mapper) {
        this.jwtProperties = jwtProperties;
        this.mapper = mapper;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException, IOException {
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

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        chain.doFilter(request, response);
        sendPrincipalInformation(response, user, token);
    }

    private void sendPrincipalInformation(HttpServletResponse response, User user, String token) throws IOException {
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        PrincipalInformation principalInformation = new PrincipalInformation(user.getUsername());
        mapper.writeValue(response.getWriter(), principalInformation);
    }

}
