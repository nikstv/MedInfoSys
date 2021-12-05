package com.stv.medinfosys.model.service;

public class CloudinaryPictureServiceModel extends BaseServiceModel{
    private String publicId;
    private String url;

    public CloudinaryPictureServiceModel() {
    }

    public String getPublicId() {
        return publicId;
    }

    public CloudinaryPictureServiceModel setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CloudinaryPictureServiceModel setUrl(String url) {
        this.url = url;
        return this;
    }
}
