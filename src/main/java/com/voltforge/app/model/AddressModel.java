package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VoltForgeAddress")
public class AddressModel {
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
    private String PINCode;

    @ManyToMany(mappedBy = "addressModel")
    private List<UserModel> users = new ArrayList<>();

    public AddressModel() {
    }

    public AddressModel(String streetName, String buildingName, String state, String country, String PINCode) {
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.state = state;
        this.country = country;
        this.PINCode = PINCode;
    }
}
