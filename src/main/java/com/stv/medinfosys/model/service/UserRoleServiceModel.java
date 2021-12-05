package com.stv.medinfosys.model.service;

import com.stv.medinfosys.model.enums.UserRoleEnum;

public class UserRoleServiceModel extends BaseServiceModel{
    private UserRoleEnum role;

    public UserRoleServiceModel() {
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public UserRoleServiceModel setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}