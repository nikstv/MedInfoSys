package com.stv.medinfosys.components;

import com.stv.medinfosys.model.binding.UserBaseBindingModel;
import com.stv.medinfosys.model.service.CloudinaryPictureServiceModel;
import com.stv.medinfosys.model.service.CountryServiceModel;
import com.stv.medinfosys.model.service.UserRoleServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.service.CloudinaryService;
import com.stv.medinfosys.service.CountryService;
import com.stv.medinfosys.service.UserRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomMapper {
    private final ModelMapper modelMapper;
    private final CountryService countryService;
    private final CloudinaryService cloudinaryService;
    private final UserRoleService userRoleService;

    public CustomMapper(ModelMapper modelMapper, CountryService countryService, CloudinaryService cloudinaryService, UserRoleService userRoleService) {
        this.modelMapper = modelMapper;
        this.countryService = countryService;
        this.cloudinaryService = cloudinaryService;
        this.userRoleService = userRoleService;
    }

    public UserServiceModel mapUserBaseBindingModelToUserServiceModel(UserBaseBindingModel userBaseBindingModel, List<Long> roles) throws IOException {
        UserServiceModel userServiceModel = this.modelMapper.map(userBaseBindingModel, UserServiceModel.class);

        CountryServiceModel countryById = this.countryService.findCountryById(userBaseBindingModel.getCountryId());
        userServiceModel.setCountry(countryById);

        if (userBaseBindingModel.getPictureMultipart() != null && !userBaseBindingModel.getPictureMultipart().isEmpty()) {
            CloudinaryPictureServiceModel uploadedPicture = this.cloudinaryService.upload(userBaseBindingModel.getPictureMultipart());
            userServiceModel.setPicture(uploadedPicture);
        }

        List<UserRoleServiceModel> roleServiceModels = roles.stream()
                .map(this.userRoleService::findRoleById)
                .collect(Collectors.toList());

        userServiceModel.setRoles(roleServiceModels);

        return userServiceModel;
    }
}