package com.stv.medinfosys.model.binding;

import javax.validation.constraints.NotBlank;

public class PhysicalExaminationBindingModel {
    private String caseHistory;
    private String laboratoryTests;
    private String therapy;
    private String healthCondition;
    private String diagnoses;

    @NotBlank
    public String getCaseHistory() {
        return caseHistory;
    }

    public PhysicalExaminationBindingModel setCaseHistory(String caseHistory) {
        this.caseHistory = caseHistory;
        return this;
    }

    @NotBlank
    public String getLaboratoryTests() {
        return laboratoryTests;
    }

    public PhysicalExaminationBindingModel setLaboratoryTests(String laboratoryTests) {
        this.laboratoryTests = laboratoryTests;
        return this;
    }

    @NotBlank
    public String getTherapy() {
        return therapy;
    }

    public PhysicalExaminationBindingModel setTherapy(String therapy) {
        this.therapy = therapy;
        return this;
    }

    @NotBlank
    public String getHealthCondition() {
        return healthCondition;
    }

    public PhysicalExaminationBindingModel setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
        return this;
    }

    @NotBlank
    public String getDiagnoses() {
        return diagnoses;
    }

    public PhysicalExaminationBindingModel setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
        return this;
    }
}
