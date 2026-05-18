package com.voltforge.app.security.response;

import java.util.List;

public class UserInfoResponseDTO {
    private String loginUsername;
    private String userFirstName;
    private String userMiddleName;
    private String userLastName;
    private String userEmail;
    private List<String> roles;

    public UserInfoResponseDTO() {
    }

    public UserInfoResponseDTO(String loginUsername, List<String> roles) {
        this.loginUsername = loginUsername;
        /*this.userFirstName = userFirstName;
        this.userMiddleName = userMiddleName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;*/
        this.roles = roles;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserMiddleName() {
        return userMiddleName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
