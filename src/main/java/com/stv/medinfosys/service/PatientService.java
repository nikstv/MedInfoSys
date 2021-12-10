package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;

import java.util.List;

public interface PatientService {
    PatientServiceModel createPatient(UserServiceModel userServiceModel);

    PatientServiceModel findPatientByUserPersonalCitizenNumber(String personalCitizenNumber);

    List<PatientServiceModel> getAllPatients();

    PatientServiceModel findPatientByUserId(Long userId);
}
