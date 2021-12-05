package com.stv.medinfosys.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "patients")
public class PatientEntity extends BaseEntity {
    //TODO DEFINE FIELDS

    private UserEntity patientProfile;

    public PatientEntity() {
    }

    public PatientEntity(UserEntity patientProfile) {
        this.patientProfile = patientProfile;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity getPatientProfile() {
        return patientProfile;
    }

    public PatientEntity setPatientProfile(UserEntity patientProfile) {
        this.patientProfile = patientProfile;
        return this;
    }
}