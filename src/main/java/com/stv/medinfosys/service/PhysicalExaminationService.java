package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.PhysicalExaminationServiceModel;

import java.util.List;

public interface PhysicalExaminationService {
    PhysicalExaminationServiceModel findPhysicalExaminationById(Long id);

    PhysicalExaminationServiceModel savePhysicalExaminationToDb(PhysicalExaminationServiceModel physicalExaminationServiceModel);

    List<PhysicalExaminationServiceModel> findAllPhysicalExaminationsByUserId(Long patientId);
}
