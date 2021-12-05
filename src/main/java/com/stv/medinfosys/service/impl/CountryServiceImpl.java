package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.service.CountryServiceModel;
import com.stv.medinfosys.repository.CountryRepository;
import com.stv.medinfosys.service.CountryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CountryServiceModel> findAllCountries() {
        List<CountryEntity> countryEntities = this.countryRepository.findAllByOrderByName();
        Type countryServiceModelListType = new TypeToken<List<CountryServiceModel>>() {
        }.getType();

        return this.modelMapper.map(countryEntities, countryServiceModelListType);
    }

    @Override
    public CountryServiceModel findCountryById(Long id) {
        Optional<CountryEntity> byId = this.countryRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("Country with id " + id + " was not found.");
        }

        return this.modelMapper.map(byId.get(), CountryServiceModel.class);
    }

    @Override
    public CountryServiceModel findCountryByName(String name) {
        Optional<CountryEntity> byNameOpt = this.countryRepository.findCountryEntityByName(name);
        if (byNameOpt.isEmpty()) {
            throw new ObjectNotFoundException("Country with name " + name + " was not found.");
        }

        return this.modelMapper.map(byNameOpt.get(), CountryServiceModel.class);
    }

    @Override
    public boolean noCountriesInDatabse() {
        return this.countryRepository.count() == 0;
    }

    @Override
    public CountryServiceModel saveCountryToDb(CountryEntity countryEntity) {
        CountryEntity save = this.countryRepository.save(countryEntity);
        return this.modelMapper.map(save, CountryServiceModel.class);
    }

}
