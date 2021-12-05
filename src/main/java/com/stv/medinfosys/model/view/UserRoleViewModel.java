package com.stv.medinfosys.model.view;

import com.stv.medinfosys.model.enums.UserRoleEnum;

public class UserRoleViewModel {
    private Long id;
    private UserRoleEnum role;

    public UserRoleViewModel() {
    }

    public Long getId() {
        return id;
    }

    public UserRoleViewModel setId(Long id) {
        this.id = id;
        return this;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public UserRoleViewModel setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}
