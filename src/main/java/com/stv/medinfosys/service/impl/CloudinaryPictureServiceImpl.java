package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.model.entity.CloudinaryPictureEntity;
import com.stv.medinfosys.model.service.CloudinaryPictureServiceModel;
import com.stv.medinfosys.repository.CloudinaryPictureRepository;
import com.stv.medinfosys.service.CloudinaryPictureService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CloudinaryPictureServiceImpl implements CloudinaryPictureService {
    private final CloudinaryPictureRepository cloudinaryPictureRepository;
    private final ModelMapper modelMapper;

    public CloudinaryPictureServiceImpl(CloudinaryPictureRepository cloudinaryPictureRepository, ModelMapper modelMapper) {
        this.cloudinaryPictureRepository = cloudinaryPictureRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CloudinaryPictureServiceModel saveToDb(CloudinaryPictureEntity cloudinaryPictureEntity){
        CloudinaryPictureEntity save = this.cloudinaryPictureRepository.save(cloudinaryPictureEntity);
        return this.modelMapper.map(save, CloudinaryPictureServiceModel.class);
    }

    @Override
    public void deleteByPublicId(String publicId){
        this.cloudinaryPictureRepository.deleteByPublicId(publicId);
    }
}
