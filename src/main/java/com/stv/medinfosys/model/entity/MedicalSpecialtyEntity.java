package com.stv.medinfosys.model.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity(name = "medical_specialties")
public class MedicalSpecialtyEntity extends BaseEntity{
    private String specialtyName;

    public MedicalSpecialtyEntity() {
    }

    @NotNull
    public String getSpecialtyName() {
        return specialtyName;
    }

    public MedicalSpecialtyEntity setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
        return this;
    }
}