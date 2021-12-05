package com.stv.medinfosys.service;

import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.UserRoleServiceModel;

import java.util.List;

public interface UserRoleService {
    UserRoleServiceModel saveToDb(UserRoleEntity userRoleEntity);

    boolean userRolesEmpty();

    UserRoleServiceModel findRoleByEnum(UserRoleEnum userRoleEnum);

    List<UserRoleServiceModel> findAllRoles();

    UserRoleServiceModel findRoleById(Long id);
}
