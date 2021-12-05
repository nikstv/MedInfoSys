package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.model.entity.DoctorEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.repository.DoctorRepository;
import com.stv.medinfosys.service.DoctorService;
import com.stv.medinfosys.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public DoctorServiceModel createDoctor(UserServiceModel userServiceModelInput){
        UserEntity userEntity = this.modelMapper.map(userServiceModelInput, UserEntity.class);

        DoctorEntity doctorEntity =
                new DoctorEntity().setDoctorProfile(userEntity);

        DoctorEntity save = this.doctorRepository.save(doctorEntity);
        return this.modelMapper.map(save, DoctorServiceModel.class);
    }

    @Override
    public DoctorServiceModel findDoctorByUserPersonalCitizenNumber(String personalCitizenNumber){
        Optional<DoctorEntity> doctorOpt =
                this.doctorRepository.findDoctorEntityByDoctorProfile_PersonalCitizenNumber(personalCitizenNumber);
        if (doctorOpt.isEmpty()){
            return null;
        }

        return this.modelMapper.map(doctorOpt.get(), DoctorServiceModel.class);
    }
}