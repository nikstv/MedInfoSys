package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.PhysicalExaminationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalExaminationRepository extends JpaRepository<PhysicalExaminationEntity, Long> {
}
