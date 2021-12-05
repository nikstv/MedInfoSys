package com.stv.medinfosys.repository;

import com.stv.medinfosys.model.entity.CloudinaryPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudinaryPictureRepository extends JpaRepository<CloudinaryPictureEntity, Long> {
    void deleteByPublicId(String publicId);
}
