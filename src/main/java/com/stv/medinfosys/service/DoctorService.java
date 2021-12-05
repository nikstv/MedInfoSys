package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;

public interface DoctorService {
    DoctorServiceModel createDoctor(UserServiceModel userServiceModelInput);

    DoctorServiceModel findDoctorByUserPersonalCitizenNumber(String personalCitizenNumber);
}
