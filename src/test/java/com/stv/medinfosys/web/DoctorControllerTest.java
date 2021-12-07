package com.stv.medinfosys.web;

import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.model.binding.PatientEditBindingModel;
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
import java.util.Objects;
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
                .setUsername("testDoctor")
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

        UserEntity patient = new UserEntity();
        patient
                .setUsername("testPatient")
                .setPassword("1234")
                .setFirstName("Ivan")
                .setMiddleName("Ivanov")
                .setLastName("Vanev")
                .setCity("Plovdiv")
                .setStreet("St. Peterburg blvd.")
                .setPersonalCitizenNumber("5425256369")
                .setIdentityDocNumber("456321489")
                .setCountry(country)
                .setNumber("133")
                .setRoles(List.of(patientRole))
                .setTelNumber("0284569698");
        UserEntity patientSaved = this.userRepository.save(patient);
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
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testRegisterPatientByDoctorWithInvalidDataFails() throws Exception {

        mockMvc
                .perform(post("/doctor/register-patient")
                        .param("firstName", "fn")
                        .param("lastName", "ln")
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
                .andExpect(flash().attributeExists("registerBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.registerBindingModel"));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testRegisterAlreadyExistingPatientByDoctor() throws Exception {

        UserEntity existingPatient = this.userRepository.findByUsername("testPatient").get();

        mockMvc
                .perform(post("/doctor/register-patient")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", existingPatient.getPersonalCitizenNumber())
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", this.patientRole.getId().toString())
                        .with(csrf())
                )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectAlreadyExistsException));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"ADMIN", "PATIENT"})
    public void testRegisterPatientByNotDoctorFails() throws Exception {
        long initialUserRepositoryCount = userRepository.count();
        mockMvc
                .perform(post("/doctor/register-patient")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitizen")
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

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testPatchPatientByDoctorPage() throws Exception {
        UserEntity patient = this.userRepository.findByUsername("testPatient").get();
        mockMvc
                .perform(get("/doctor/edit-patient-details/" + patient.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userId", patient.getId()))
                .andExpect(model().attribute("postLink", "/doctor/edit-patient-details/" + patient.getId()))
                .andExpect(model().attributeExists("editBindingModel", "allCountries", "allRoles"));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testPatchPatientByDoctor() throws Exception {
        UserEntity patient = this.userRepository.findByUsername("testPatient").get();
        String patchedFirstName = "patchedFirstName";
        String patchedLastName = "patchedLastName";

        mockMvc
                .perform(patch("/doctor/edit-patient-details/" + patient.getId())
                        .param("firstName", patchedFirstName)
                        .param("lastName", patchedLastName)
                        .param("personalCitizenNumber", "5636589654")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection());

        patient = this.userRepository.findByUsername("testPatient").get();
        assertEquals(patient.getFirstName(), patchedFirstName);
        assertEquals(patient.getLastName(), patchedLastName);
        UserRoleEnum expectedPatientRole = patient.getRoles().stream()
                .map(UserRoleEntity::getRole)
                .filter(r -> r.equals(UserRoleEnum.PATIENT))
                .findAny()
                .orElse(null);
        assertEquals(expectedPatientRole, UserRoleEnum.PATIENT);
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testPatchPatientByDoctorWithInvalidDataFails() throws Exception {
        UserEntity patient = this.userRepository.findByUsername("testPatient").get();
        String patchedFirstName = "pfn";
        String patchedLastName = "pln";

        mockMvc
                .perform(patch("/doctor/edit-patient-details/" + patient.getId())
                        .param("firstName", patchedFirstName)
                        .param("lastName", patchedLastName)
                        .param("personalCitizenNumber", "5636589654")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("editBindingModel", "org.springframework.validation.BindingResult.editBindingModel"));
    }
}