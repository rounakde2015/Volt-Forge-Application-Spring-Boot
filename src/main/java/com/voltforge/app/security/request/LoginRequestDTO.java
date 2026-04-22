package com.voltforge.app.security.request;

public class LoginRequestDTO {
    private String loginUsername;
    private String loginPassword;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String loginUsername, String loginPassword) {
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
