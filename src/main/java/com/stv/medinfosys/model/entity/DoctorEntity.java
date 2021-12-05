package com.stv.medinfosys.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class DoctorEntity extends BaseEntity {
    private UserEntity doctorProfile;
    private List<MedicalSpecialtyEntity> specialties;

    public DoctorEntity() {
        this.specialties = new ArrayList<>();
    }

    public DoctorEntity(UserEntity doctorProfile) {
        this.specialties = new ArrayList<>();
        this.doctorProfile = doctorProfile;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity getDoctorProfile() {
        return doctorProfile;
    }

    public DoctorEntity setDoctorProfile(UserEntity doctorProfile) {
        this.doctorProfile = doctorProfile;
        return this;
    }

    @ManyToMany
    public List<MedicalSpecialtyEntity> getSpecialties() {
        return specialties;
    }

    public DoctorEntity setSpecialties(List<MedicalSpecialtyEntity> specialties) {
        this.specialties = specialties;
        return this;
    }
}