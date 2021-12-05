package com.stv.medinfosys.model.service;

public class PatientServiceModel extends BaseServiceModel {
    private UserServiceModel patientProfile;

    public PatientServiceModel() {
    }

    public UserServiceModel getPatientProfile() {
        return patientProfile;
    }

    public PatientServiceModel setPatientProfile(UserServiceModel patientProfile) {
        this.patientProfile = patientProfile;
        return this;
    }
}