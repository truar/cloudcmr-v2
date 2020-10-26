package com.cloud.cmr.configuration.security.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("doFilter:: authenticating...");

        var authToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authToken == null || authToken.isEmpty()) {
            filterChain.doFilter(httpRequest, response);
            return;
        }

        try {
            var authentication = getAndValidateAuthentication(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("doFilter():: successfully authenticated.");
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("Fail to authenticate.", ex);
            return ;
        }

        filterChain.doFilter(httpRequest, response);
    }

    protected abstract Authentication getAndValidateAuthentication(String authToken) throws Exception;

}
