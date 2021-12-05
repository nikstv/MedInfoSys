package com.stv.medinfosys.model.service;

import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class DoctorServiceModel extends BaseServiceModel {
    private UserEntity doctorProfile;
    private List<MedicalSpecialtyEntity> specialties;

    public DoctorServiceModel() {
        this.specialties = new ArrayList<>();
    }

    public UserEntity getDoctorProfile() {
        return doctorProfile;
    }

    public DoctorServiceModel setDoctorProfile(UserEntity doctorProfile) {
        this.doctorProfile = doctorProfile;
        return this;
    }

    public List<MedicalSpecialtyEntity> getSpecialties() {
        return specialties;
    }

    public DoctorServiceModel setSpecialties(List<MedicalSpecialtyEntity> specialties) {
        this.specialties = specialties;
        return this;
    }
}