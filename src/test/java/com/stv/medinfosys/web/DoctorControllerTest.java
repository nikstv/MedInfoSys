package com.stv.medinfosys.web;

import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DoctorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    CountryEntity country = null;
    UserRoleEntity doctorRole = null;
    UserRoleEntity patientRole = null;

    @BeforeEach
    void init() {
        this.country = this.countryRepository.save(new CountryEntity().setName("testName").setNativeName("testNativeName"));
        this.doctorRole = this.userRoleRepository.findByRole(UserRoleEnum.DOCTOR).get();
        this.patientRole = this.userRoleRepository.findByRole(UserRoleEnum.PATIENT).get();

        UserEntity doctor = new UserEntity();
        doctor
                .setUsername("testUser")
                .setPassword("1234")
                .setFirstName("Nikolay")
                .setMiddleName("Nikolaev")
                .setLastName("Nikolov")
                .setCity("Plovdiv")
                .setStreet("St. Peterburg blvd.")
                .setPersonalCitizenNumber("test_8565252563")
                .setIdentityDocNumber("456321489")
                .setCountry(country)
                .setNumber("133")
                .setRoles(List.of(doctorRole))
                .setTelNumber("0284569698");
        UserEntity doctorSaved = this.userRepository.save(doctor);
    }

    @AfterEach
    void clean() {
        this.doctorRepository.deleteAll();
        this.patientRepository.deleteAll();
        this.userRepository.deleteAll();
        this.countryRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void registerPatientPageAvailableForDoctors() throws Exception {
        mockMvc
                .perform(get("/doctor/register-patient"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allCountries", "postLink"));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"ADMIN", "PATIET"})
    public void registerPatientPageNotAvailableForPatientAndAdmin() throws Exception {
        mockMvc
                .perform(get("/doctor/register-patient"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testRegisterPatientByDoctor() throws Exception {

        long initialUserRepositoryCount = userRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(post("/doctor/register-patient")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", this.patientRole.getId().toString())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("initialUsername"))
                .andExpect(flash().attributeExists("initialPassword"))
                .andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        if (location != null) {
            long patientId = Long.parseLong(location.split("\\/")[2]);
            Optional<UserEntity> byId = this.userRepository.findById(patientId);
            if (byId.isPresent()) {
                List<UserRoleEntity> roles = byId.get().getRoles();
                assertEquals(1, roles.size());
                assertEquals(this.patientRole.getId(), roles.get(0).getId());
                assertEquals(this.patientRole.getRole(), roles.get(0).getRole());
            }
        }

        assertEquals(initialUserRepositoryCount + 1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"ADMIN", "PATIENT"})
    public void testRegisterPatientByNotDoctorFails() throws Exception {
        long initialUserRepositoryCount = userRepository.count();
        mockMvc
                .perform(post("/doctor/register-patient")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", this.patientRole.getId().toString())
                        .with(csrf())
                )
                .andExpect(status().is4xxClientError());

        assertEquals(initialUserRepositoryCount, userRepository.count());
    }
}