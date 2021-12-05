package com.stv.medinfosys.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class CountryEntity extends BaseEntity{
    private String name;
    private String nativeName;

    public CountryEntity() {
    }

    @Column(unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public CountryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getNativeName() {
        return nativeName;
    }

    public CountryEntity setNativeName(String nativeName) {
        this.nativeName = nativeName;
        return this;
    }
}