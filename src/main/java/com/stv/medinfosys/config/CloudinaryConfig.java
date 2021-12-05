package com.stv.medinfosys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "medinfosys.cloudinary")
public class CloudinaryConfig {
    private String apiLink;

    public String getApiLink() {
        return apiLink;
    }

    public CloudinaryConfig setApiLink(String apiLink) {
        this.apiLink = apiLink;
        return this;
    }
}
