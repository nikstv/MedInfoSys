package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.PatientEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.repository.PatientRepository;
import com.stv.medinfosys.repository.UserRepository;
import com.stv.medinfosys.service.PatientService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private final ModelMapper modelMapper;
    private final PatientRepository patientRepository;

    public PatientServiceImpl(ModelMapper modelMapper, PatientRepository patientRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientServiceModel createPatient(UserServiceModel userServiceModelInput) {
        UserEntity userEntity = this.modelMapper.map(userServiceModelInput, UserEntity.class);

        PatientEntity patientEntity = new PatientEntity()
                .setPatientProfile(userEntity);

        PatientEntity save = this.patientRepository.save(patientEntity);
        return modelMapper.map(save, PatientServiceModel.class);
    }

    @Override
    public PatientServiceModel findPatientByUserPersonalCitizenNumber(String personalCitizenNumber) {
        Optional<PatientEntity> patientOpt
                = this.patientRepository.findPatientEntityByPatientProfile_PersonalCitizenNumber(personalCitizenNumber);
        if (patientOpt.isEmpty()) {
            return null;
        }

        return modelMapper.map(patientOpt.get(), PatientServiceModel.class);
    }

    @Override
    public List<PatientServiceModel> getAllPatients() {
        List<PatientEntity> all = this.patientRepository.findAll();
        Type type = new TypeToken<List<PatientServiceModel>>() {
        }.getType();
        return this.modelMapper.map(all, type);
    }

    @Override
    public PatientServiceModel findPatientByUserId(Long userId) {
        Optional<PatientEntity> patientOpt
                = this.patientRepository.findPatientEntityByPatientProfile_Id(userId);
        if (patientOpt.isEmpty()) {
            throw new ObjectNotFoundException("User with id " + userId + " is not patient.");
        }

        return modelMapper.map(patientOpt.get(), PatientServiceModel.class);
    }
}