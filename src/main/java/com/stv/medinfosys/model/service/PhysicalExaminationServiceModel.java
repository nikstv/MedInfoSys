package com.stv.medinfosys.model.service;

public class PhysicalExaminationServiceModel {
    private DoctorServiceModel doctor;
    private PatientServiceModel patient;

    public PhysicalExaminationServiceModel() {
    }

    public DoctorServiceModel getDoctor() {
        return doctor;
    }

    public PhysicalExaminationServiceModel setDoctor(DoctorServiceModel doctor) {
        this.doctor = doctor;
        return this;
    }

    public PatientServiceModel getPatient() {
        return patient;
    }

    public PhysicalExaminationServiceModel setPatient(PatientServiceModel patient) {
        this.patient = patient;
        return this;
    }
}
