package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "VoltForgeUsers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_mobile_number", "user_enail_id"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_signin_name")
    @NotBlank
    @Size(min = 5, max = 50)
    private String userName;

    @Column(name = "user_first_name")
    @NotBlank
    @Size(max = 50)
    private String userFirstName;

    @Column(name = "user_middle_name")
    @Size(max = 50)
    private String userMiddleName;

    @Column(name = "user_last_name")
    @NotBlank
    @Size(max = 50)
    private String userLastName;

    @Column(name = "user_email_id")
    @NotBlank
    @Size(max = 50)
    @Email(message = "Invalid Email")
    private String userEmail;

    @Column(name = "user_mobile_number")
    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^(?:\\+91|91)?[1-9][0-9]{9}$", message = "Invalid mobile number")
    private String userMobileNumber;

    @Column(name = "user_password", length = 100)
    @NotBlank
    private String userPassword;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "volt_forge_users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> userRoles = new HashSet<Role>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    /*@JoinTable(name = "volt_forge_user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))*/
    private List<Address> address = new ArrayList<Address>();


    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private Set<Product> products;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,  CascadeType.REFRESH})
    private Cart cart;

    public User() {
    }

    public User(String userName,
                String userFirstName,
                String userMiddleName,
                String userLastName,
                String userPassword,
                String userMobileNumber,
                String userEmail) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userMiddleName = userMiddleName;
        this.userLastName = userLastName;
        this.userPassword = userPassword;
        this.userMobileNumber = userMobileNumber;
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
