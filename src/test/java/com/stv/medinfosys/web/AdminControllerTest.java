package com.stv.medinfosys.web;

import com.stv.medinfosys.model.binding.UserEditBindingModel;
import com.stv.medinfosys.model.entity.BaseEntity;
import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.repository.CountryRepository;
import com.stv.medinfosys.repository.DoctorRepository;
import com.stv.medinfosys.repository.PatientRepository;
import com.stv.medinfosys.repository.UserRepository;
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
class AdminControllerTest {
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

    CountryEntity country = null;

    @BeforeEach
    void init() {
        this.country = this.countryRepository.save(new CountryEntity().setName("testName").setNativeName("testNativeName"));

        UserEntity userEntity = new UserEntity();
        userEntity
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
                .setRoles(List.of())
                .setTelNumber("0284569698");
        UserEntity save = this.userRepository.save(userEntity);
    }

    @AfterEach
    void clean() {
        this.doctorRepository.deleteAll();
        this.patientRepository.deleteAll();
        this.userRepository.deleteAll();
        this.countryRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUsersAdminPanelWithAdminRole() throws Exception {
        mockMvc
                .perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"PATIENT", "DOCTOR"})
    public void testUsersAdminPanelWithoutAdminRole() throws Exception {
        mockMvc
                .perform(get("/admin/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void registerUserAccountByAdminPage() throws Exception {
        mockMvc
                .perform(get("/admin/user/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allCountries", "userRoles", "postLink"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testRegisterUserAccountByAdmin() throws Exception {

        long initialUserRepositoryCount = userRepository.count();

        mockMvc
                .perform(post("/admin/user/register")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", "1")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("initialUsername"))
                .andExpect(flash().attributeExists("initialPassword"));
        assertEquals(initialUserRepositoryCount + 1, userRepository.count());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testRegisterUserAccountByAdminWithIncorrectDataFails() throws Exception {

        long initialUserRepositoryCount = userRepository.count();

        mockMvc
                .perform(post("/admin/user/register")
                        .param("firstName", "f")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", "1")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("registerBindingModel"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.registerBindingModel"));
        assertEquals(initialUserRepositoryCount, userRepository.count());
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    public void testRegisterUserAccountByNoAdminFails() throws Exception {

        long initialUserRepositoryCount = userRepository.count();

        mockMvc
                .perform(post("/admin/user/register")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("country", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("roles", "1")
                        .with(csrf())
                )
                .andExpect(status().isForbidden());
        assertEquals(initialUserRepositoryCount, userRepository.count());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUserPatch() throws Exception {
        String editedFirstName = "editedFirstName";
        String editedLastName = "editedLastName";
        UserEntity userEntity = this.userRepository.findByUsername("testUser").get();

        mockMvc
                .perform(patch("/admin/user/" + userEntity.getId() + "/edit")
                        .param("firstName", editedFirstName)
                        .param("lastName", editedLastName)
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("rolesId", "1")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection());

        UserEntity userEntityUpdated = this.userRepository.findByUsername("testUser").get();
        assertEquals(userEntityUpdated.getFirstName(), editedFirstName);
        assertEquals(userEntityUpdated.getLastName(), editedLastName);

        long[] roles = userEntityUpdated.getRoles().stream().map(BaseEntity::getId).mapToLong(Long::longValue).toArray();
        assertArrayEquals(roles, new long[]{1});
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUserPatchWithIncorrectData() throws Exception {
        String editedFirstName = "e";
        String editedLastName = "editedLastName";
        UserEntity userEntity = this.userRepository.findByUsername("testUser").get();

        mockMvc
                .perform(patch("/admin/user/" + userEntity.getId() + "/edit")
                        .param("firstName", editedFirstName)
                        .param("lastName", editedLastName)
                        .param("personalCitizenNumber", "personalCitimber")
                        .param("identityDocNumber", "testIdDocNumber")
                        .param("telNumber", "telNumber")
                        .param("countryId", this.country.getId().toString())
                        .param("city", "city")
                        .param("street", "street")
                        .param("number", "number")
                        .param("rolesId", "1")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/user/"+userEntity.getId()+"/edit-err"))
                .andExpect(flash().attributeExists("editBindingModel", "org.springframework.validation.BindingResult.editBindingModel"));

        UserEntity userEntityUpdated = this.userRepository.findByUsername("testUser").get();
        assertNotEquals(userEntityUpdated.getFirstName(), editedFirstName);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void patchUserAccountByAdminPage() throws Exception {

        UserEntity userEntity = this.userRepository.findByUsername("testUser").get();

        mockMvc
                .perform(get("/admin/user/" + userEntity.getId() + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userId", "editBindingModel", "allCountries", "allRoles", "postLink"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void patchUserAccountByAdminBindingErrorPage() throws Exception {

        UserEntity userEntity = this.userRepository.findByUsername("testUser").get();

        mockMvc
                .perform(get("/admin/user/" + userEntity.getId() + "/edit-err").flashAttr("editBindingModel", new UserEditBindingModel()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userId", "allCountries", "allRoles", "postLink"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testChangeUserDetails() throws Exception {
        UserEntity userEntity = this.userRepository.findByUsername("testUser").get();
        mockMvc
                .perform(patch("/admin/user/reset-login-details")
                        .param("id", userEntity.getId().toString()).with(csrf()))
                .andExpect(status().is3xxRedirection());
        UserEntity afterChange = this.userRepository.findByUsername("testUser").get();
        assertEquals(afterChange.getUsername(), userEntity.getUsername());
        assertNotEquals(afterChange.getPassword(), userEntity.getPassword());
    }
}