package com.stv.medinfosys.model.binding;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UserEditBindingModel extends UserBaseBindingModel {

    private List<Long> rolesId;

    public UserEditBindingModel() {
        this.rolesId = new ArrayList<>();
    }

    @NotEmpty
    public List<Long> getRolesId() {
        return rolesId;
    }

    public UserEditBindingModel setRolesId(List<Long> rolesId) {
        this.rolesId = rolesId;
        return this;
    }
}