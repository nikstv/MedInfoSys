package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;

import java.util.List;

public interface DoctorService {
    DoctorServiceModel createDoctor(UserServiceModel userServiceModelInput);

    DoctorServiceModel findDoctorByUserPersonalCitizenNumber(String personalCitizenNumber);

    DoctorServiceModel findDoctorProfileByUserId(Long userId);

    void patchDoctorMedicalSpecialties(Long doctorId, List<Long> specialtiesIds);

    DoctorServiceModel findDoctorByUserUsername(String username);
}
