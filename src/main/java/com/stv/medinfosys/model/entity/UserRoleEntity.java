package com.stv.medinfosys.model.entity;

import com.stv.medinfosys.model.enums.UserRoleEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {
    private UserRoleEnum role;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRoleEnum role) {
        this.role = role;
    }

    @Enumerated(EnumType.STRING)
    public UserRoleEnum getRole() {
        return role;
    }

    public UserRoleEntity setRole(UserRoleEnum role) {
        this.role = role;
        return this;
    }
}