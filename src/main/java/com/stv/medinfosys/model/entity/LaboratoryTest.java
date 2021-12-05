package com.stv.medinfosys.model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "laboratory_tests")
public class LaboratoryTest extends BaseEntity{
    //TODO ADD FIELDS

    private DoctorEntity doctor;
    private PatientEntity patient;

    public LaboratoryTest() {
    }

    @ManyToOne
    public DoctorEntity getDoctor() {
        return doctor;
    }

    @ManyToOne
    public PatientEntity getPatient() {
        return patient;
    }

    public LaboratoryTest setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        return this;
    }

    public LaboratoryTest setPatient(PatientEntity patient) {
        this.patient = patient;
        return this;
    }
}