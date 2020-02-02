package com.cloud.cmr.security.web;

class PrincipalInformation {
    private String username;

    public PrincipalInformation(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
