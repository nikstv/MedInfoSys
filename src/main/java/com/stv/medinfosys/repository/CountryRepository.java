package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    List<CountryEntity> findAllByOrderByName();
    Optional<CountryEntity> findCountryEntityByName(String name);
}