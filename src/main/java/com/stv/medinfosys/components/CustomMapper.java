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
import org.springframework.ui.Model;

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
        userServiceModel.setAccountNonLocked(true);
        userServiceModel.setEnabled(true);

        return userServiceModel;
    }

    public void mapUserServiceModelToViewModel(Model model, UserServiceModel userByIdServiceModel) {
        if (!model.containsAttribute("initialPassword") && !model.containsAttribute("initialUsername")) {
            model.addAttribute("initialPassword", null);
            model.addAttribute("initialUsername", null);
        }

        StringBuilder fullName = new StringBuilder()
                .append("Full name: ")
                .append(userByIdServiceModel.getFirstName()).append(" ")
                .append(userByIdServiceModel.getMiddleName()).append(" ")
                .append(userByIdServiceModel.getLastName());
        model.addAttribute("fullName", fullName);

        String address = "Country: " + userByIdServiceModel.getCountry().getName() +
                "; State: " + userByIdServiceModel.getState() +
                "; Municipality: " + userByIdServiceModel.getMunicipality() +
                "; City: " + userByIdServiceModel.getCity() +
                "; District: " + userByIdServiceModel.getDistrict() +
                "; Street: " + userByIdServiceModel.getStreet() +
                "; Number: " + userByIdServiceModel.getNumber() +
                "; Additional info: " + userByIdServiceModel.getAdditionalInfo();
        model.addAttribute("address", address);

        StringBuilder personalCitizenNumber = new StringBuilder()
                .append("Personal citizen number: ")
                .append(userByIdServiceModel.getPersonalCitizenNumber());
        model.addAttribute("personalCitizenNumber", personalCitizenNumber);

        StringBuilder phoneNumber = new StringBuilder()
                .append("Phone number: ").append(userByIdServiceModel.getTelNumber());
        model.addAttribute("phoneNumber", phoneNumber);

        StringBuilder idDocNumber = new StringBuilder()
                .append("ID document number: ").append(userByIdServiceModel.getIdentityDocNumber());
        model.addAttribute("idDocNumber", idDocNumber);
        model.addAttribute("userId", userByIdServiceModel.getId());

        if (userByIdServiceModel.getPicture() != null) {
            model.addAttribute("profilePicture", userByIdServiceModel.getPicture().getUrl());
        }
    }

}