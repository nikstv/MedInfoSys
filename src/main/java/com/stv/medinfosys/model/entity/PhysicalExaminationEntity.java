package com.stv.medinfosys.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "physical_examinations")
public class PhysicalExaminationEntity extends BaseEntity{
    private DoctorEntity doctor;
    private PatientEntity patient;
//    private String caseHistory;
//    private String examinationType; //prophylaxis, primaryExamination, secondaryExamination
//    private String laboratoryTests;
//    private String therapy;
//    private String healthCondition;

    public PhysicalExaminationEntity() {
    }

    @ManyToOne
    public DoctorEntity getDoctor() {
        return doctor;
    }

    public PhysicalExaminationEntity setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        return this;
    }

    @ManyToOne
    public PatientEntity getPatient() {
        return patient;
    }

    public PhysicalExaminationEntity setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }
}