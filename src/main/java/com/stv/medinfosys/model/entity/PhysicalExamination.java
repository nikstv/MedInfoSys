package com.stv.medinfosys.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "physical_examinations")
public class PhysicalExamination extends BaseEntity{
    private DoctorEntity doctor;
    private PatientEntity patient;
    private String caseHistory;
    private String examinationType; //prophylaxis, primaryExamination, secondaryExamination
    private String laboratoryTests;
    private String therapy;
    private String healthCondition;

    public PhysicalExamination() {
    }

    @ManyToOne
    public DoctorEntity getDoctor() {
        return doctor;
    }

    public PhysicalExamination setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        return this;
    }

    @ManyToOne
    public PatientEntity getPatient() {
        return patient;
    }

    public PhysicalExamination setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }
}