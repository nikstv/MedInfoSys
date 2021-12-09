package com.stv.medinfosys.service;

import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;

import java.util.List;

public interface MedicalSpecialtyService {
    boolean noMedicalSpecialtiesInDb();

    MedicalSpecialtyServiceModel saveToDb(MedicalSpecialtyEntity medicalSpecialtyEntity);

    List<MedicalSpecialtyServiceModel> getAllMedicalSpecialties();

    MedicalSpecialtyServiceModel findSpecialtyById(Long specialtyId);
}
