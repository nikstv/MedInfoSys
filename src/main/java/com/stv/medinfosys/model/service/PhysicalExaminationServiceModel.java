package com.stv.medinfosys.model.service;

public class PhysicalExaminationServiceModel extends BaseServiceModel{
    private DoctorServiceModel doctor;
    private PatientServiceModel patient;

    private String caseHistory;
    private String laboratoryTests;
    private String therapy;
    private String healthCondition;
    private String diagnoses;

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

    public String getCaseHistory() {
        return caseHistory;
    }

    public PhysicalExaminationServiceModel setCaseHistory(String caseHistory) {
        this.caseHistory = caseHistory;
        return this;
    }

    public String getLaboratoryTests() {
        return laboratoryTests;
    }

    public PhysicalExaminationServiceModel setLaboratoryTests(String laboratoryTests) {
        this.laboratoryTests = laboratoryTests;
        return this;
    }

    public String getTherapy() {
        return therapy;
    }

    public PhysicalExaminationServiceModel setTherapy(String therapy) {
        this.therapy = therapy;
        return this;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public PhysicalExaminationServiceModel setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
        return this;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public PhysicalExaminationServiceModel setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
        return this;
    }
}
