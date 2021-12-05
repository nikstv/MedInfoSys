package com.stv.medinfosys.model.service;

public class CountryServiceModel extends BaseServiceModel{
    private String name;
    private String nativeName;

    public CountryServiceModel() {
    }

    public String getName() {
        return name;
    }

    public CountryServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getNativeName() {
        return nativeName;
    }

    public CountryServiceModel setNativeName(String nativeName) {
        this.nativeName = nativeName;
        return this;
    }
}
