package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.model.entity.PatientEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.repository.PatientRepository;
import com.stv.medinfosys.repository.UserRepository;
import com.stv.medinfosys.service.PatientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatientServiceImplTest {
    PatientRepository mockPatientRepo;
    UserRepository mockUserRepo;
    PatientService patientService;

    @BeforeEach
    private void init() {
        this.mockPatientRepo = Mockito.mock(PatientRepository.class);
        this.mockUserRepo=Mockito.mock(UserRepository.class);
        this.patientService= new PatientServiceImpl(new ModelMapper(), mockPatientRepo, mockUserRepo);
    }

    @AfterEach
    private void clean(){
        this.mockPatientRepo.deleteAll();
        this.mockUserRepo.deleteAll();
    }

    @Test
    public void testFindAllPatientsAsServiceModels(){
        UserEntity userEntity = new UserEntity().setUsername("testUser");

        PatientEntity patientEntity1 = new PatientEntity();
        patientEntity1.setId(1L);
        patientEntity1.setPatientProfile(userEntity);

        PatientEntity patientEntity2 = new PatientEntity();
        patientEntity2.setId(2L);
        patientEntity2.setPatientProfile(userEntity);

        PatientEntity patientEntity3 = new PatientEntity();
        patientEntity3.setId(3L);
        patientEntity3.setPatientProfile(userEntity);

        Mockito.when(mockPatientRepo.findAll()).thenReturn(List.of(patientEntity1, patientEntity2, patientEntity3));

        List<PatientServiceModel> allPatientsServiceModels = patientService.getAllPatients();
        int expectedListSize = 3;

        assertEquals(expectedListSize, allPatientsServiceModels.size());
        for (PatientServiceModel patientsServiceModel : allPatientsServiceModels) {
            assertEquals("testUser", patientsServiceModel.getPatientProfile().getUsername());
        }
    }

    @Test
    public void testFindPatientByUserId(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("test").setFirstName("testFirstName");
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setPatientProfile(userEntity);

        Mockito.when(this.mockPatientRepo.findPatientEntityByPatientProfile_Id(1L)).thenReturn(Optional.of(patientEntity));
        PatientServiceModel patientByUserId = this.patientService.findPatientByUserId(1L);

        assertEquals("test", patientByUserId.getPatientProfile().getUsername());
        assertEquals("testFirstName", patientByUserId.getPatientProfile().getFirstName());
    }
}