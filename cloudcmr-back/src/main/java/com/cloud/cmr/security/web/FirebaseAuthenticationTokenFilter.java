package com.cloud.cmr.security.web;

import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Authenticate a User on Firebase based on the Token ID.
 * Generates a Cookie for the user
 */
public class FirebaseAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationTokenFilter.class);
    private final static String TOKEN_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("doFilter:: authenticating...");

        String authToken = httpRequest.getHeader(TOKEN_HEADER);

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
        }

        filterChain.doFilter(httpRequest, response);
    }

    /**
     *
     * @param authToken Firebase access token string
     * @return the computed result
     * @throws Exception
     */
    private Authentication getAndValidateAuthentication(String authToken) throws Exception {
        var firebaseToken = FirebaseAuth.getInstance().verifyIdToken(authToken);
        return new UsernamePasswordAuthenticationToken(firebaseToken, authToken, new ArrayList<>());
    }

    @Override
    public void destroy() {
        logger.debug("destroy():: invoke");
    }

}