package com.stv.medinfosys.init;

import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.entity.MedicalSpecialtyEntity;
import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.CountryServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.service.CountryService;
import com.stv.medinfosys.service.MedicalSpecialtyService;
import com.stv.medinfosys.service.UserRoleService;
import com.stv.medinfosys.service.UserService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CountryService countryService;
    private final MedicalSpecialtyService medicalSpecialtyService;
    private final UserService userService;

    public DatabaseInit(UserRoleService userRoleService, PasswordEncoder passwordEncoder, ModelMapper modelMapper, CountryService countryService, MedicalSpecialtyService medicalSpecialtyService, UserService userService) {
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.countryService = countryService;
        this.medicalSpecialtyService = medicalSpecialtyService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        initCountries();
        initMedicalSpecialties();
        initUserRoles();
        initUsers();
    }

    private void initMedicalSpecialties() throws FileNotFoundException, ParseException {
        if (!this.medicalSpecialtyService.noMedicalSpecialtiesInDb()) {
            return;
        }

        FileInputStream fileInputStream = new FileInputStream("src/main/java/com/stv/medinfosys/init/medialSpecialties.json");
        JSONParser jsonParser = new JSONParser(fileInputStream);
        List<Map<String, String>> parse = (List<Map<String, String>>) jsonParser.parse();
        for (Map<String, String> specialty : parse) {
            MedicalSpecialtyEntity specialtyEntity = new MedicalSpecialtyEntity().setSpecialtyName(specialty.get("specialtyName"));
            this.medicalSpecialtyService.saveToDb(specialtyEntity);
        }
    }

    private void initCountries() throws FileNotFoundException, ParseException {
        if (!this.countryService.noCountriesInDatabse()) {
            return;
        }

        FileInputStream fileInputStream = new FileInputStream("src/main/java/com/stv/medinfosys/init/countries.json");
        JSONParser jsonParser = new JSONParser(fileInputStream);
        List<Map<String, String>> parse = (List<Map<String, String>>) jsonParser.parse();
        for (Map<String, String> country : parse) {
            CountryEntity countryEntity = new CountryEntity().setName(country.get("name")).setNativeName(country.get("native"));
            this.countryService.saveCountryToDb(countryEntity);
        }
    }

    private void initUsers() {
        if (!this.userService.noUsersInDb()) {
            return;
        }

        CountryServiceModel Bulgaria = this.countryService.findCountryByName("Bulgaria");

        UserServiceModel admin = new UserServiceModel();
        admin
                .setUsername("admin")
                .setPassword("1234")
                .setFirstName("Nikolay")
                .setMiddleName("Nikolaev")
                .setLastName("Nikolov")
                .setCity("Plovdiv")
                .setStreet("St. Peterburg blvd.")
                .setPersonalCitizenNumber("8565252563")
                .setIdentityDocNumber("456321489")
                .setCountry(Bulgaria)
                .setNumber("133")
                .setRoles(List.of(this.userRoleService.findRoleByEnum(UserRoleEnum.ADMIN)))
                .setTelNumber("0284569698")
                .setEnabled(true)
                .setAccountNonLocked(true);

        this.userService.saveToDb(admin);

        UserServiceModel doctor = new UserServiceModel();
        doctor
                .setUsername("doctor")
                .setPassword("1234")
                .setFirstName("Ivan")
                .setMiddleName("Ivankov")
                .setLastName("Ivanov")
                .setCity("Sofiqa")
                .setStreet("Vitosha")
                .setPersonalCitizenNumber("3654159852")
                .setIdentityDocNumber("654125365")
                .setCountry(Bulgaria)
                .setNumber("254")
                .setRoles(List.of(this.userRoleService.findRoleByEnum(UserRoleEnum.DOCTOR)))
                .setTelNumber("0326541452")
                .setEnabled(true)
                .setAccountNonLocked(true);

        this.userService.saveToDb(doctor);
    }

    private void initUserRoles() {
        if (!this.userRoleService.userRolesEmpty()) {
            return;
        }

        UserRoleEntity admin = new UserRoleEntity(UserRoleEnum.ADMIN);
        userRoleService.saveToDb(admin);

        UserRoleEntity doctor = new UserRoleEntity(UserRoleEnum.DOCTOR);
        userRoleService.saveToDb(doctor);

        UserRoleEntity patient = new UserRoleEntity(UserRoleEnum.PATIENT);
        userRoleService.saveToDb(patient);
    }
}