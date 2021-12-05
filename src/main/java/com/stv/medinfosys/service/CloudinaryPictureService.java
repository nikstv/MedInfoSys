package com.stv.medinfosys.service;

import com.stv.medinfosys.model.entity.CloudinaryPictureEntity;
import com.stv.medinfosys.model.service.CloudinaryPictureServiceModel;

public interface CloudinaryPictureService {
    CloudinaryPictureServiceModel saveToDb(CloudinaryPictureEntity cloudinaryPictureEntity);

    void deleteByPublicId(String publicId);
}
