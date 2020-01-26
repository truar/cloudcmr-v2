package com.cloud.cmr.authentication.web;

import com.cloud.cmr.authentication.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class AuthenticationController {

    public static final String AUTH_TOKEN_NAME = "authToken";
    @Value("${cookie.duration.in.second}")
    public int COOKIE_DURATION;

    @PostMapping("/login")
    @ResponseStatus(NO_CONTENT)
    public void authenticate(@RequestBody UserCredentials userCredentials, HttpServletResponse response) {
        String value = generateValidTokenFor(userCredentials);
        Cookie cookie = new Cookie(AUTH_TOKEN_NAME, value);
        cookie.setMaxAge(COOKIE_DURATION);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private String generateValidTokenFor(UserCredentials userCredentials) {
        return "CHANGEME";
    }

}
