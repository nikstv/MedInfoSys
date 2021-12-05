package com.stv.medinfosys.service;

import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;

public interface MedicalSpecialtyService {
    boolean noMedicalSpecialtiesInDb();

    MedicalSpecialtyServiceModel saveToDb(MedicalSpecialtyEntity medicalSpecialtyEntity);
}
