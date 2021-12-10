package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.PhysicalExaminationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExaminationEntity, Long> {
    List<PhysicalExaminationEntity> findAllByPatient_PatientProfile_Id(Long patient_patientProfile_id);
}
