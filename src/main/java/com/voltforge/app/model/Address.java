package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "VoltForgeAddress")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Column(name = "street_name")
    @Size(min = 5, message = "Street name should be of atleast 5 characters")
    private String streetName;

    @NotBlank
    @Column(name = "building_name")
    @Size(min = 5, message = "Building name should be of atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Column(name = "city")
    @Size(min = 5, message = "Building name should be of atleast 5 characters")
    private String city;

    @NotBlank
    @Column(name = "state")
    @Size(min = 5, message = "State name should be of atleast 5 characters")
    private String state;

    @NotBlank
    @Column(name = "country")
    @Size(min = 2, message = "Country name should be of atleast 2 characters")
    private String country;

    @NotBlank
    @Column(name = "PIN_code")
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[1-9][0-9]{2}\\s?[0-9]{3}$", message = "Invalid PIN Code")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }

    public Address(String streetName, String buildingName, String state, String city, String country, String pinCode) {
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
