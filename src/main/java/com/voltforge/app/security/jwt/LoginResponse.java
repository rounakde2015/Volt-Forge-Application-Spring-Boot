package com.voltforge.app.security.jwt;

import java.util.List;

public class LoginResponse {
    private String jwtToken;
    private String loginUsername;
    private List<String> roles;

    public LoginResponse() {
    }

    public LoginResponse(String jwtToken, String loginUsername, List<String> roles) {
        this.jwtToken = jwtToken;
        this.loginUsername = loginUsername;
        this.roles = roles;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
