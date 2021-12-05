package com.stv.medinfosys.model.service;

public class MedicalSpecialtyServiceModel extends BaseServiceModel{
    private String specialtyName;

    public MedicalSpecialtyServiceModel() {
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public MedicalSpecialtyServiceModel setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
        return this;
    }
}
