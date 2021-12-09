package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.view.AdminPanelUserViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByPersonalCitizenNumber(String personalCitizenNumber);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.role='PATIENT'")
    List<UserEntity> findAllPatients();

    List<UserEntity> findAllByEnabledIsFalseAndAnonymousIsFalse();
}