package com.stv.medinfosys.model.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public abstract class UserBaseBindingModel {
    private String firstName;
    private String middleName;
    private String lastName;
    private String personalCitizenNumber;
    private String identityDocNumber;
    private String telNumber;
    private Long countryId;
    private String state;
    private String municipality;
    private String city;
    private String district;
    private String street;
    private String number;
    private String additionalInfo;
    private MultipartFile pictureMultipart;

    public UserBaseBindingModel() {
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getFirstName() {
        return firstName;
    }

    public UserBaseBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public UserBaseBindingModel setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getLastName() {
        return lastName;
    }

    public UserBaseBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @NotNull
    @Positive
    public Long getCountryId() {
        return countryId;
    }

    public UserBaseBindingModel setCountryId(Long countryId) {
        this.countryId = countryId;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getPersonalCitizenNumber() {
        return personalCitizenNumber;
    }

    public UserBaseBindingModel setPersonalCitizenNumber(String personalCitizenNumber) {
        this.personalCitizenNumber = personalCitizenNumber;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getIdentityDocNumber() {
        return identityDocNumber;
    }

    public UserBaseBindingModel setIdentityDocNumber(String identityDocNumber) {
        this.identityDocNumber = identityDocNumber;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getTelNumber() {
        return telNumber;
    }

    public UserBaseBindingModel setTelNumber(String telNumber) {
        this.telNumber = telNumber;
        return this;
    }

    public String getState() {
        return state;
    }

    public UserBaseBindingModel setState(String state) {
        this.state = state;
        return this;
    }

    public String getMunicipality() {
        return municipality;
    }

    public UserBaseBindingModel setMunicipality(String municipality) {
        this.municipality = municipality;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getCity() {
        return city;
    }

    public UserBaseBindingModel setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public UserBaseBindingModel setDistrict(String district) {
        this.district = district;
        return this;
    }

    @NotBlank
    @Size(min = 4, max = 20)
    public String getStreet() {
        return street;
    }

    public UserBaseBindingModel setStreet(String street) {
        this.street = street;
        return this;
    }

    @NotBlank
    @Size(min = 1, max = 10)
    public String getNumber() {
        return number;
    }

    public UserBaseBindingModel setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public UserBaseBindingModel setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public MultipartFile getPictureMultipart() {
        return pictureMultipart;
    }

    public UserBaseBindingModel setPictureMultipart(MultipartFile pictureMultipart) {
        this.pictureMultipart = pictureMultipart;
        return this;
    }
}