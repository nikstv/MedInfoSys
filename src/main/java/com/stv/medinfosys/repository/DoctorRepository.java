package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    Optional<DoctorEntity> findDoctorEntityByDoctorProfile_PersonalCitizenNumber(String doctorProfile_personalCitizenNumber);
    Optional<DoctorEntity> findDoctorEntityByDoctorProfile_Id(Long doctorProfile_id);
    Optional<DoctorEntity> findDoctorEntityByDoctorProfile_Username(String doctorProfile_username);
}