package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.UserRoleServiceModel;
import com.stv.medinfosys.repository.UserRoleRepository;
import com.stv.medinfosys.service.UserRoleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRoleServiceModel saveToDb(UserRoleEntity userRoleEntity) {
        UserRoleEntity save = this.userRoleRepository.save(userRoleEntity);
        return modelMapper.map(save, UserRoleServiceModel.class);
    }

    @Override
    public boolean userRolesEmpty() {
        return this.userRoleRepository.count() == 0;
    }

    @Override
    public UserRoleServiceModel findRoleByEnum(UserRoleEnum userRoleEnum) {
        return this.userRoleRepository.findAll()
                .stream()
                .filter(role -> role.getRole().equals(userRoleEnum))
                .findAny()
                .map(role -> this.modelMapper.map(role, UserRoleServiceModel.class))
                .orElseThrow(() -> new ObjectNotFoundException("User role not found."));
    }

    @Override
    public List<UserRoleServiceModel> findAllRoles() {
        List<UserRoleEntity> all = this.userRoleRepository.findAll();

        Type listOfUserRoleServiceModel = new TypeToken<List<UserRoleServiceModel>>() {
        }.getType();

        return this.modelMapper.map(all, listOfUserRoleServiceModel);
    }

    @Override
    public UserRoleServiceModel findRoleById(Long id) {
        Optional<UserRoleEntity> byId = this.userRoleRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("ROle with id " + id + " was not found.");
        }

        return modelMapper.map(byId.get(), UserRoleServiceModel.class);
    }

}