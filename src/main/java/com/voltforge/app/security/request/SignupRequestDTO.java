package com.voltforge.app.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class SignupRequestDTO {
    @NotBlank
    @Size(min = 10, max = 20)
    private String userName;

    @NotBlank
    @Size(min = 10, max = 20)
    private String userFirstName;

    @Size(min = 10, max = 20)
    private String userMiddleName;

    @NotBlank
    @Size(min = 10, max = 20)
    private String userLastName;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^(?:\\+91|91)?[1-9][0-9]{9}$", message = "Invalid mobile number")
    private String userMobileNumber;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Email
    @Size(min = 10, max = 20)
    private String userEmail;

    private Set<String> userRoles;

    public SignupRequestDTO() {
    }

    public SignupRequestDTO(String userName,
                            String userFirstName,
                            String userMiddleName,
                            String userLastName,
                            String userMobileNumber,
                            String password,
                            String userEmail,
                            Set<String> userRoles) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userMiddleName = userMiddleName;
        this.userLastName = userLastName;
        this.userMobileNumber = userMobileNumber;
        this.password = password;
        this.userEmail = userEmail;
        this.userRoles = userRoles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Set<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<String> userRoles) {
        this.userRoles = userRoles;
    }
}
