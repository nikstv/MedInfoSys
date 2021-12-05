package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.CloudinaryPictureServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    CloudinaryPictureServiceModel upload(MultipartFile file) throws IOException;

    boolean deleteImage(String publicId);
}
