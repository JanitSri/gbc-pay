package com.COMP3095.gbc_pay.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street cannot be Empty")
    private String street;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Country cannot be empty")
    private String country;

    private boolean defaultShipping;

    private boolean defaultBilling;

    @OneToOne
    private Profile profile;

    public Address() {
    }

    public Address(String street, String city, String country, boolean defaultShipping, boolean defaultBilling) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.defaultShipping = defaultShipping;
        this.defaultBilling = defaultBilling;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDefaultShipping() {
        return defaultShipping;
    }

    public void setDefaultShipping(boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public boolean isDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(boolean defaultBilling) {
        this.defaultBilling = defaultBilling;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
