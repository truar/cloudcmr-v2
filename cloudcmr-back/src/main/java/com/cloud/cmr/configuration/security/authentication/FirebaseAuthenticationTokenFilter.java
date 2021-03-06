package com.cloud.cmr.configuration.security.authentication;

import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Authenticate a User on Firebase based on the Token ID.
 */
@Component
@Profile("!test")
public class FirebaseAuthenticationTokenFilter extends AuthenticationTokenFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationTokenFilter.class);
    public static final String BEARER = "Bearer ";

    /**
     * @param authToken Firebase access token string
     * @return the computed result
     * @throws Exception
     */
    protected Authentication getAndValidateAuthentication(String authToken) throws Exception {
        var token = authToken.substring(BEARER.length());
        var firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return new UsernamePasswordAuthenticationToken(firebaseToken.getEmail(), authToken, new ArrayList<>());
    }

    @Override
    public void destroy() {
        logger.debug("destroy():: invoke");
    }

}