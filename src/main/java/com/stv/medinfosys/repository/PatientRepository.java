package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findPatientEntityByPatientProfile_PersonalCitizenNumber(String patientProfile_personalCitizenNumber);
    Optional<PatientEntity> findPatientEntityByPatientProfile_Id(Long patientProfile_id);
}
