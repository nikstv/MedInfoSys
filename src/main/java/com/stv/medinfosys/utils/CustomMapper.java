package com.stv.medinfosys.utils;

import com.stv.medinfosys.model.binding.UserBaseBindingModel;
import com.stv.medinfosys.model.service.*;
import com.stv.medinfosys.model.view.PhysicalExaminationViewModel;
import com.stv.medinfosys.model.view.UserInfoViewModel;
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
        userServiceModel.setAccountNonLocked(true);
        userServiceModel.setEnabled(true);
        userServiceModel.setAnonymous(false);

        return userServiceModel;
    }

    public UserInfoViewModel mapUserServiceModelToViewModel(UserServiceModel userByIdServiceModel) {
        String fullName = "Full name: " +
                userByIdServiceModel.getFirstName() + " " +
                userByIdServiceModel.getMiddleName() + " " +
                userByIdServiceModel.getLastName();

        String address = "Country: " + userByIdServiceModel.getCountry().getName() +
                "; State: " + userByIdServiceModel.getState() +
                "; Municipality: " + userByIdServiceModel.getMunicipality() +
                "; City: " + userByIdServiceModel.getCity() +
                "; District: " + userByIdServiceModel.getDistrict() +
                "; Street: " + userByIdServiceModel.getStreet() +
                "; Number: " + userByIdServiceModel.getNumber() +
                "; Additional info: " + userByIdServiceModel.getAdditionalInfo();

        String personalCitizenNumber = "Personal citizen number: " +
                userByIdServiceModel.getPersonalCitizenNumber();

        String phoneNumber = "Phone number: " + userByIdServiceModel.getTelNumber();

        String idDocNumber = "ID document number: " + userByIdServiceModel.getIdentityDocNumber();

        UserInfoViewModel userInfoViewModel = new UserInfoViewModel()
                .setFullName(fullName)
                .setAddress(address)
                .setPersonalCitizenNumber(personalCitizenNumber)
                .setPhoneNumber(phoneNumber)
                .setIdDocNumber(idDocNumber)
                .setUserId(userByIdServiceModel.getId().toString())
                .setAccountNonLocked(userByIdServiceModel.getAccountNonLocked());

        if (userByIdServiceModel.getPicture() != null) {
            userInfoViewModel.setProfilePicture(userByIdServiceModel.getPicture().getUrl());
        }

        return userInfoViewModel;
    }

    public PhysicalExaminationViewModel mapPhysicalExaminationServiceToViewModel(PhysicalExaminationServiceModel physicalExaminationServiceModel) {
        PhysicalExaminationViewModel map = this.modelMapper.map(physicalExaminationServiceModel, PhysicalExaminationViewModel.class);

        UserServiceModel doctorProfile = physicalExaminationServiceModel.getDoctor().getDoctorProfile();
        map.setDoctor(doctorProfile.getFirstName() + " " + doctorProfile.getLastName());

        UserServiceModel patientProfile = physicalExaminationServiceModel.getPatient().getPatientProfile();
        map.setPatient(patientProfile.getFirstName() + " " + patientProfile.getLastName());

        return map;
    }

}