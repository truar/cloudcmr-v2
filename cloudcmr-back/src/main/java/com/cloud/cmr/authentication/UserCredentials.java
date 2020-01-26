package com.cloud.cmr.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCredentials {
    private String username;
    private String password;

    @JsonCreator
    public UserCredentials(@JsonProperty String username,
                           @JsonProperty String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentials() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
