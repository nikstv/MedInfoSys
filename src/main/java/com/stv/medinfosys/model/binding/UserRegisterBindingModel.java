package com.stv.medinfosys.model.binding;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UserRegisterBindingModel extends UserBaseBindingModel{

    private List<Long> roles;

    public UserRegisterBindingModel() {
        this.roles = new ArrayList<>();
    }

    @NotEmpty
    public List<Long> getRoles() {
        return roles;
    }

    public UserRegisterBindingModel setRoles(List<Long> roles) {
        this.roles = roles;
        return this;
    }
}