package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.DoctorEntity;
import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.repository.DoctorRepository;
import com.stv.medinfosys.service.DoctorService;
import com.stv.medinfosys.service.MedicalSpecialtyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final MedicalSpecialtyService medicalSpecialtyService;

    public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper, MedicalSpecialtyService medicalSpecialtyService) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.medicalSpecialtyService = medicalSpecialtyService;
    }

    @Override
    public DoctorServiceModel createDoctor(UserServiceModel userServiceModelInput) {
        UserEntity userEntity = this.modelMapper.map(userServiceModelInput, UserEntity.class);

        DoctorEntity doctorEntity =
                new DoctorEntity().setDoctorProfile(userEntity);

        DoctorEntity save = this.doctorRepository.save(doctorEntity);
        return this.modelMapper.map(save, DoctorServiceModel.class);
    }

    @Override
    public DoctorServiceModel findDoctorByUserPersonalCitizenNumber(String personalCitizenNumber) {
        Optional<DoctorEntity> doctorOpt =
                this.doctorRepository.findDoctorEntityByDoctorProfile_PersonalCitizenNumber(personalCitizenNumber);
        if (doctorOpt.isEmpty()) {
            return null;
        }

        return this.modelMapper.map(doctorOpt.get(), DoctorServiceModel.class);
    }

    @Override
    @Transactional
    public DoctorServiceModel findDoctorProfileByUserId(Long userId) {
        Optional<DoctorEntity> doctorEntityByDoctorProfile_id = this.doctorRepository.findDoctorEntityByDoctorProfile_Id(userId);
        if (doctorEntityByDoctorProfile_id.isEmpty()) {
            throw new ObjectNotFoundException("Doctor profile for user id " + userId + " was not found");
        }

        return this.modelMapper.map(doctorEntityByDoctorProfile_id.get(), DoctorServiceModel.class);
    }

    @Override
    public void patchDoctorMedicalSpecialties(Long doctorId, List<Long> specialtiesIds) {
        Optional<DoctorEntity> doctorOpt = this.doctorRepository.findById(doctorId);
        List<MedicalSpecialtyEntity> medicalSpecialtyEntities = new ArrayList<>();
        if (doctorOpt.isEmpty()) {
            throw new ObjectNotFoundException("Doctor with id " + doctorId + " was not found");
        }

        for (Long id : specialtiesIds) {
            MedicalSpecialtyServiceModel specialty = this.medicalSpecialtyService.findSpecialtyById(id);
            medicalSpecialtyEntities.add(this.modelMapper.map(specialty, MedicalSpecialtyEntity.class));
        }

        DoctorEntity doctor = doctorOpt.get().setSpecialties(medicalSpecialtyEntities);
        this.doctorRepository.save(doctor);
    }

    @Override
    @Transactional
    public DoctorServiceModel findDoctorByUserUsername(String username) {
        Optional<DoctorEntity> doctorOpt =
                this.doctorRepository.findDoctorEntityByDoctorProfile_Username(username);
        if (doctorOpt.isEmpty()) {
            throw new ObjectNotFoundException("Doctor with username " + username + " was not found");
        }

        return this.modelMapper.map(doctorOpt.get(), DoctorServiceModel.class);
    }
}