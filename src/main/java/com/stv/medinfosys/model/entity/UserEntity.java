package com.stv.medinfosys.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    private String username;
    private String password;
    private List<UserRoleEntity> roles;
    private String firstName;
    private String middleName;
    private String lastName;
    private String personalCitizenNumber;
    private String identityDocNumber;
    private String telNumber;
    private CountryEntity country;
    private String state;
    private String municipality;
    private String city;
    private String district;
    private String street;
    private String number;
    private String additionalInfo;
    private CloudinaryPictureEntity picture;
    private Boolean isEnabled;
    private Boolean isAccountNonLocked;
    private Boolean isAnonymous;

    public UserEntity() {
        this.roles=new ArrayList<>();
    }

    public void addRole(UserRoleEntity roleEntity) {
        this.roles.add(roleEntity);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public UserEntity setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    @Column(nullable = false)
    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Column(nullable = false, unique = true)
    public String getPersonalCitizenNumber() {
        return personalCitizenNumber;
    }

    public UserEntity setPersonalCitizenNumber(String personalCitizenNumber) {
        this.personalCitizenNumber = personalCitizenNumber;
        return this;
    }

    @Column(nullable = false)
    public String getIdentityDocNumber() {
        return identityDocNumber;
    }

    public UserEntity setIdentityDocNumber(String identityDocNumber) {
        this.identityDocNumber = identityDocNumber;
        return this;
    }

    @Column(nullable = false)
    public String getTelNumber() {
        return telNumber;
    }

    public UserEntity setTelNumber(String telNumber) {
        this.telNumber = telNumber;
        return this;
    }

    @ManyToOne(optional = false)
    public CountryEntity getCountry() {
        return country;
    }

    public UserEntity setCountry(CountryEntity country) {
        this.country = country;
        return this;
    }

    public String getState() {
        return state;
    }

    public UserEntity setState(String state) {
        this.state = state;
        return this;
    }

    public String getMunicipality() {
        return municipality;
    }

    public UserEntity setMunicipality(String municipality) {
        this.municipality = municipality;
        return this;
    }

    @Column(nullable = false)
    public String getCity() {
        return city;
    }

    public UserEntity setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public UserEntity setDistrict(String district) {
        this.district = district;
        return this;
    }

    @Column(nullable = false)
    public String getStreet() {
        return street;
    }

    public UserEntity setStreet(String street) {
        this.street = street;
        return this;
    }

    @Column(nullable = false)
    public String getNumber() {
        return number;
    }

    public UserEntity setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public UserEntity setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    @OneToOne
    public CloudinaryPictureEntity getPicture() {
        return picture;
    }

    public UserEntity setPicture(CloudinaryPictureEntity picture) {
        this.picture = picture;
        return this;
    }

    @Column(nullable = false)
    public Boolean getEnabled() {
        return isEnabled;
    }

    public UserEntity setEnabled(Boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    @Column(nullable = false)
    public Boolean getAccountNonLocked() {
        return isAccountNonLocked;
    }

    public UserEntity setAccountNonLocked(Boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
        return this;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public UserEntity setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
        return this;
    }
}