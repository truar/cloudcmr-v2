package com.cloud.cmr.exposition;

import com.cloud.cmr.security.authentication.AuthenticationTokenFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;

@Component
@Profile("test")
public class Base64AuthenticationTokenFilter extends AuthenticationTokenFilter {

    @Override
    protected Authentication getAndValidateAuthentication(String authToken) {
        String decoded = new String(Base64.getDecoder().decode(authToken.substring(6)));
        String[] auth = decoded.split(":");
        String username = auth[0];
        String password = auth[1];
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }
}
