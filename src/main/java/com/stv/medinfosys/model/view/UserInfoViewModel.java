package com.stv.medinfosys.model.view;

public class UserInfoViewModel {
    private String fullName;
    private String address;
    private String personalCitizenNumber;
    private String phoneNumber;
    private String idDocNumber;
    private String userId;
    private String profilePicture;

    public UserInfoViewModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public UserInfoViewModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserInfoViewModel setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPersonalCitizenNumber() {
        return personalCitizenNumber;
    }

    public UserInfoViewModel setPersonalCitizenNumber(String personalCitizenNumber) {
        this.personalCitizenNumber = personalCitizenNumber;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserInfoViewModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public UserInfoViewModel setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserInfoViewModel setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public UserInfoViewModel setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }
}
