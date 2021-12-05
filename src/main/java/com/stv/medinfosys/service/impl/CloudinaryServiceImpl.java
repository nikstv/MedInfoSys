package com.stv.medinfosys.service.impl;

import com.cloudinary.Cloudinary;
import com.stv.medinfosys.model.entity.CloudinaryPictureEntity;
import com.stv.medinfosys.model.service.CloudinaryPictureServiceModel;
import com.stv.medinfosys.service.CloudinaryPictureService;
import com.stv.medinfosys.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final CloudinaryPictureService cloudinaryPictureService;
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(CloudinaryPictureService cloudinaryPictureService, Cloudinary cloudinary) {
        this.cloudinaryPictureService = cloudinaryPictureService;
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryPictureServiceModel upload(MultipartFile file) throws IOException {
        Map upload = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
        CloudinaryPictureEntity cloudinaryPictureEntity =
                new CloudinaryPictureEntity()
                        .setPublicId(upload.get("public_id").toString())
                        .setUrl(upload.get("url").toString());
        return this.cloudinaryPictureService.saveToDb(cloudinaryPictureEntity);
    }

    @Override
    public boolean deleteImage(String publicId) {
        try {
            Map destroy = this.cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            return false;
        }

        this.cloudinaryPictureService.deleteByPublicId(publicId);
        return true;
    }
}