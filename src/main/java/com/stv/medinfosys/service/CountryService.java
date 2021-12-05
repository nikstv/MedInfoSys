package com.stv.medinfosys.service;

import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.service.CountryServiceModel;

import java.util.List;

public interface CountryService {
    List<CountryServiceModel> findAllCountries();

    CountryServiceModel findCountryById(Long id);

    CountryServiceModel findCountryByName(String name);

    boolean noCountriesInDatabse();

    CountryServiceModel saveCountryToDb(CountryEntity countryEntity);
}
