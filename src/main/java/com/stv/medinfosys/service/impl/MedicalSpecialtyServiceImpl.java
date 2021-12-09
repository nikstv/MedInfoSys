package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;
import com.stv.medinfosys.repository.MedicalSpecialtyRepository;
import com.stv.medinfosys.service.MedicalSpecialtyService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<MedicalSpecialtyServiceModel> getAllMedicalSpecialties() {
        List<MedicalSpecialtyEntity> all = this.medicalSpecialtyRepository.findAll();
        Type type = new TypeToken<List<MedicalSpecialtyServiceModel>>() {
        }.getType();
        return this.modelMapper.map(all, type);
    }

    @Override
    public MedicalSpecialtyServiceModel findSpecialtyById(Long specialtyId) {
        Optional<MedicalSpecialtyEntity> byId = this.medicalSpecialtyRepository.findById(specialtyId);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("Specialty with id " + specialtyId + " was not found.");
        }

        return this.modelMapper.map(byId.get(), MedicalSpecialtyServiceModel.class);
    }
}
