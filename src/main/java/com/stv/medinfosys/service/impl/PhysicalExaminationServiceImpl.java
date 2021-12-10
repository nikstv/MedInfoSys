package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.PhysicalExaminationEntity;
import com.stv.medinfosys.model.service.PhysicalExaminationServiceModel;
import com.stv.medinfosys.repository.PhysicalExaminationRepository;
import com.stv.medinfosys.service.PhysicalExaminationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class PhysicalExaminationServiceImpl implements PhysicalExaminationService {
    private final PhysicalExaminationRepository physicalExaminationRepository;
    private final ModelMapper modelMapper;

    public PhysicalExaminationServiceImpl(PhysicalExaminationRepository physicalExaminationRepository, ModelMapper modelMapper) {
        this.physicalExaminationRepository = physicalExaminationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public PhysicalExaminationServiceModel findPhysicalExaminationById(Long id) {
        Optional<PhysicalExaminationEntity> byId = this.physicalExaminationRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("Physical examination with id " + id + " was not found.");
        }

        return this.modelMapper.map(byId.get(), PhysicalExaminationServiceModel.class);
    }

    @Override
    public PhysicalExaminationServiceModel savePhysicalExaminationToDb(PhysicalExaminationServiceModel physicalExaminationServiceModel) {
        PhysicalExaminationEntity map = this.modelMapper.map(physicalExaminationServiceModel, PhysicalExaminationEntity.class);
        PhysicalExaminationEntity save = this.physicalExaminationRepository.save(map);
        return this.modelMapper.map(save, PhysicalExaminationServiceModel.class);
    }

    @Override
    @Transactional
    public List<PhysicalExaminationServiceModel> findAllPhysicalExaminationsByUserId(Long userId){
        List<PhysicalExaminationEntity> allByPatient_id = this.physicalExaminationRepository.findAllByPatient_PatientProfile_Id(userId);
        Type type = new TypeToken<List<PhysicalExaminationServiceModel>>() {
        }.getType();
        return this.modelMapper.map(allByPatient_id, type);
    }
}
