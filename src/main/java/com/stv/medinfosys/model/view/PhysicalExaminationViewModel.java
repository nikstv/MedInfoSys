package com.stv.medinfosys.model.view;

public class PhysicalExaminationViewModel {
    private Long id;
    private String doctor;
    private String patient;
    private String caseHistory;
    private String laboratoryTests;
    private String therapy;
    private String healthCondition;
    private String diagnoses;

    public PhysicalExaminationViewModel() {
    }

    public String getDoctor() {
        return doctor;
    }

    public PhysicalExaminationViewModel setDoctor(String doctor) {
        this.doctor = doctor;
        return this;
    }

    public String getPatient() {
        return patient;
    }

    public PhysicalExaminationViewModel setPatient(String patient) {
        this.patient = patient;
        return this;
    }

    public String getCaseHistory() {
        return caseHistory;
    }

    public PhysicalExaminationViewModel setCaseHistory(String caseHistory) {
        this.caseHistory = caseHistory;
        return this;
    }

    public String getLaboratoryTests() {
        return laboratoryTests;
    }

    public PhysicalExaminationViewModel setLaboratoryTests(String laboratoryTests) {
        this.laboratoryTests = laboratoryTests;
        return this;
    }

    public String getTherapy() {
        return therapy;
    }

    public PhysicalExaminationViewModel setTherapy(String therapy) {
        this.therapy = therapy;
        return this;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public PhysicalExaminationViewModel setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
        return this;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public PhysicalExaminationViewModel setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
        return this;
    }

    public Long getId() {
        return id;
    }

    public PhysicalExaminationViewModel setId(Long id) {
        this.id = id;
        return this;
    }
}
