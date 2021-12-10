package com.stv.medinfosys.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "physical_examinations")
public class PhysicalExaminationEntity extends BaseEntity{
    private DoctorEntity doctor;
    private PatientEntity patient;

    private String caseHistory;
    private String laboratoryTests;
    private String therapy;
    private String healthCondition;
    private String diagnoses;


    public PhysicalExaminationEntity() {
    }

    @ManyToOne(optional = false)
    public DoctorEntity getDoctor() {
        return doctor;
    }

    public PhysicalExaminationEntity setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        return this;
    }

    @ManyToOne(optional = false)
    public PatientEntity getPatient() {
        return patient;
    }

    public PhysicalExaminationEntity setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }


    @Lob
    @NotNull
    public String getCaseHistory() {
        return caseHistory;
    }

    public PhysicalExaminationEntity setCaseHistory(String caseHistory) {
        this.caseHistory = caseHistory;
        return this;
    }

    @Lob
    @NotNull
    public String getLaboratoryTests() {
        return laboratoryTests;
    }

    public PhysicalExaminationEntity setLaboratoryTests(String laboratoryTests) {
        this.laboratoryTests = laboratoryTests;
        return this;
    }

    @Lob
    @NotNull
    public String getTherapy() {
        return therapy;
    }

    public PhysicalExaminationEntity setTherapy(String therapy) {
        this.therapy = therapy;
        return this;
    }

    @Lob
    @NotNull
    public String getHealthCondition() {
        return healthCondition;
    }

    public PhysicalExaminationEntity setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
        return this;
    }

    @NotNull
    public String getDiagnoses() {
        return diagnoses;
    }

    public PhysicalExaminationEntity setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
        return this;
    }
}