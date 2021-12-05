package com.stv.medinfosys.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "physical_examinations")
public class PhysicalExamination extends BaseEntity{
    private DoctorEntity doctor;
    private PatientEntity patient;

    //TODO DEFINE FIELDS


    public PhysicalExamination() {
    }

    @ManyToOne
    public DoctorEntity getDoctor() {
        return doctor;
    }

    @ManyToOne
    public PatientEntity getPatient() {
        return patient;
    }

    public PhysicalExamination setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        return this;
    }

    public PhysicalExamination setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }
}