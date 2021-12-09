package com.stv.medinfosys.model.service;

import java.util.ArrayList;
import java.util.List;

public class UserServiceModel extends BaseServiceModel {
    private String username;
    private String password;
    private List<UserRoleServiceModel> roles;

    private String firstName;
    private String middleName;
    private String lastName;

    private String personalCitizenNumber;
    private String identityDocNumber;

    private String telNumber;

    private CountryServiceModel country;
    private String state;
    private String municipality;
    private String city;
    private String district;
    private String street;
    private String number;
    private String additionalInfo;
    private CloudinaryPictureServiceModel picture;
    private Boolean isEnabled;
    private Boolean isAccountNonLocked;
    private String initialRawPassword;
    private Boolean isAnonymous;

    public UserServiceModel() {
        this.roles = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public UserServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<UserRoleServiceModel> getRoles() {
        return roles;
    }

    public UserServiceModel setRoles(List<UserRoleServiceModel> roles) {
        this.roles = roles;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public UserServiceModel setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPersonalCitizenNumber() {
        return personalCitizenNumber;
    }

    public UserServiceModel setPersonalCitizenNumber(String personalCitizenNumber) {
        this.personalCitizenNumber = personalCitizenNumber;
        return this;
    }

    public String getIdentityDocNumber() {
        return identityDocNumber;
    }

    public UserServiceModel setIdentityDocNumber(String identityDocNumber) {
        this.identityDocNumber = identityDocNumber;
        return this;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public UserServiceModel setTelNumber(String telNumber) {
        this.telNumber = telNumber;
        return this;
    }

    public CountryServiceModel getCountry() {
        return country;
    }

    public UserServiceModel setCountry(CountryServiceModel country) {
        this.country = country;
        return this;
    }

    public String getState() {
        return state;
    }

    public UserServiceModel setState(String state) {
        this.state = state;
        return this;
    }

    public String getMunicipality() {
        return municipality;
    }

    public UserServiceModel setMunicipality(String municipality) {
        this.municipality = municipality;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserServiceModel setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public UserServiceModel setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public UserServiceModel setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public UserServiceModel setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public UserServiceModel setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public String getInitialRawPassword() {
        return initialRawPassword;
    }

    public UserServiceModel setInitialRawPassword(String initialRawPassword) {
        this.initialRawPassword = initialRawPassword;
        return this;
    }

    public CloudinaryPictureServiceModel getPicture() {
        return picture;
    }

    public UserServiceModel setPicture(CloudinaryPictureServiceModel picture) {
        this.picture = picture;
        return this;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public UserServiceModel setEnabled(Boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public UserServiceModel setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
        return this;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public UserServiceModel setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
        return this;
    }
}