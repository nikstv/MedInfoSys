package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;
import com.stv.medinfosys.repository.MedicalSpecialtyRepository;
import com.stv.medinfosys.service.MedicalSpecialtyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MedicalSpecialtyServiceImpl implements MedicalSpecialtyService {
    private final MedicalSpecialtyRepository medicalSpecialtyRepository;
    private final ModelMapper modelMapper;

    public MedicalSpecialtyServiceImpl(MedicalSpecialtyRepository medicalSpecialtyRepository, ModelMapper modelMapper) {
        this.medicalSpecialtyRepository = medicalSpecialtyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean noMedicalSpecialtiesInDb() {
        return this.medicalSpecialtyRepository.count() == 0;
    }

    @Override
    public MedicalSpecialtyServiceModel saveToDb(MedicalSpecialtyEntity medicalSpecialtyEntity) {
        MedicalSpecialtyEntity save = this.medicalSpecialtyRepository.save(medicalSpecialtyEntity);
        return this.modelMapper.map(save, MedicalSpecialtyServiceModel.class);
    }
}
