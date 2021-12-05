package com.stv.medinfosys.model.view;

import java.util.List;

public class AdminPanelUserViewModel {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String personalCitizenNumber;
    private List<UserRoleViewModel> roles;

    public AdminPanelUserViewModel() {
    }

    public Long getId() {
        return id;
    }

    public AdminPanelUserViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AdminPanelUserViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public List<UserRoleViewModel> getRoles() {
        return roles;
    }

    public AdminPanelUserViewModel setRoles(List<UserRoleViewModel> roles) {
        this.roles = roles;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AdminPanelUserViewModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AdminPanelUserViewModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPersonalCitizenNumber() {
        return personalCitizenNumber;
    }

    public AdminPanelUserViewModel setPersonalCitizenNumber(String personalCitizenNumber) {
        this.personalCitizenNumber = personalCitizenNumber;
        return this;
    }
}
