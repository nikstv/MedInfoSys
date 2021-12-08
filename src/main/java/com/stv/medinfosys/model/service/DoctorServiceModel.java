package com.stv.medinfosys.model.service;

import java.util.ArrayList;
import java.util.List;

public class DoctorServiceModel extends BaseServiceModel {
    private UserServiceModel doctorProfile;
    private List<MedicalSpecialtyServiceModel> specialties;

    public DoctorServiceModel() {
        this.specialties = new ArrayList<>();
    }

    public UserServiceModel getDoctorProfile() {
        return doctorProfile;
    }

    public DoctorServiceModel setDoctorProfile(UserServiceModel doctorProfile) {
        this.doctorProfile = doctorProfile;
        return this;
    }

    public List<MedicalSpecialtyServiceModel> getSpecialties() {
        return specialties;
    }

    public DoctorServiceModel setSpecialties(List<MedicalSpecialtyServiceModel> specialties) {
        this.specialties = specialties;
        return this;
    }
}