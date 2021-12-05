package com.stv.medinfosys.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "profile_pictures")
public class CloudinaryPictureEntity extends BaseEntity {
    private String publicId;
    private String url;

    public CloudinaryPictureEntity() {
    }

    public String getPublicId() {
        return publicId;
    }

    public CloudinaryPictureEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CloudinaryPictureEntity setUrl(String url) {
        this.url = url;
        return this;
    }
}
