package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.PhysicalExaminationEntity;
import com.stv.medinfosys.model.service.PhysicalExaminationServiceModel;
import com.stv.medinfosys.repository.PhysicalExaminationRepository;
import com.stv.medinfosys.service.PhysicalExaminationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
            throw new ObjectNotFoundException("Physical examnation with id " + id + " was not found.");
        }

        return this.modelMapper.map(byId.get(), PhysicalExaminationServiceModel.class);
    }
}
