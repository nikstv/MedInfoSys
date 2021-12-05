package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalSpecialtyRepository extends JpaRepository<MedicalSpecialtyEntity, Long> {
}
