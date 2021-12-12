package com.stv.medinfosys.web;

import com.stv.medinfosys.model.entity.*;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
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

    @Autowired
    PhysicalExaminationRepository physicalExaminationRepository;

    @Autowired
    MedicalSpecialtyRepository medicalSpecialtyRepository;

    CountryEntity country = null;
    UserRoleEntity doctorRole = null;
    UserRoleEntity patientRole = null;
    PhysicalExaminationEntity physicalExaminationEntity = null;

    @BeforeEach
    void init() {
        this.country = this.countryRepository.save(new CountryEntity().setName("testName").setNativeName("testNativeName"));
        this.doctorRole = this.userRoleRepository.findByRole(UserRoleEnum.DOCTOR).get();
        this.patientRole = this.userRoleRepository.findByRole(UserRoleEnum.PATIENT).get();

        UserEntity doctorSaved = this.userRepository.save(new UserEntity()
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
                .setTelNumber("0284569698")
                .setEnabled(true)
                .setAccountNonLocked(true));

        UserEntity patientSaved = this.userRepository.save(new UserEntity()
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
                .setTelNumber("0284569698")
                .setEnabled(true)
                .setAccountNonLocked(true));

        PatientEntity patientEntitySaved =
                this.patientRepository.save(new PatientEntity().setPatientProfile(patientSaved));

        DoctorEntity doctorEntitySaved =
                this.doctorRepository.save(
                        new DoctorEntity().
                                setDoctorProfile(doctorSaved)
                                .setSpecialties(this.medicalSpecialtyRepository.findAll())
                );

        this.physicalExaminationEntity = this.physicalExaminationRepository.save(new PhysicalExaminationEntity()
                .setCaseHistory("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDiagnoses("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDoctor(doctorEntitySaved)
                .setHealthCondition("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setLaboratoryTests("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setPatient(patientEntitySaved)
                .setTherapy("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
    }

    @AfterEach
    void clean() {
        this.physicalExaminationRepository.deleteAll();
        this.doctorRepository.deleteAll();
        this.patientRepository.deleteAll();
        this.userRepository.deleteAll();
        this.countryRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"ADMIN", "DOCTOR"})
    public void testPhysicalExaminationInfoPage() throws Exception {
        Long physicalExaminationEntityId = this.physicalExaminationEntity.getId();
        mockMvc
                .perform(get("/user/physical-examination/info/" + physicalExaminationEntityId))
                .andExpect(view().name("physical-examination-info"))
                .andExpect(model().attributeExists("patient", "doctor", "medicalSpecialties", "physicalExamination"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testPatient", roles = {"PATIENT"})
    public void testPhysicalExaminationInfoPageByPatient() throws Exception {
        Long physicalExaminationEntityId = this.physicalExaminationEntity.getId();
        mockMvc
                .perform(get("/user/physical-examination/info/" + physicalExaminationEntityId))
                .andExpect(view().name("physical-examination-info"))
                .andExpect(model().attributeExists("patient", "doctor", "medicalSpecialties", "physicalExamination"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testPatient", roles = {"PATIENT"})
    public void testUserDetailsPageViewByPatient() throws Exception {
        UserEntity patientUserEntity = this.userRepository.findByUsername("testPatient").get();
        mockMvc
                .perform(get("/user/" + patientUserEntity.getId() + "/details"))
                .andExpect(view().name("user-details-view"))
                .andExpect(model().attributeExists("userInfoViewModel", "physicalExaminations"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testDoctor", roles = {"DOCTOR"})
    public void testUserDetailsPageViewByDoctor() throws Exception {
        UserEntity patientUserEntity = this.userRepository.findByUsername("testDoctor").get();
        mockMvc
                .perform(get("/user/" + patientUserEntity.getId() + "/details"))
                .andExpect(view().name("user-details-view"))
                .andExpect(model().attributeExists("userInfoViewModel", "specialties"))
                .andExpect(model().attribute("isDoctor", true))
                .andExpect(status().isOk());
    }
}