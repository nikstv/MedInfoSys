package com.stv.medinfosys.web;

import com.stv.medinfosys.components.CustomMapper;
import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.model.binding.PatientEditBindingModel;
import com.stv.medinfosys.model.binding.PatientRegisterBindingModel;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.*;
import com.stv.medinfosys.model.view.UserInfoViewModel;
import com.stv.medinfosys.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DoctorController {
    private final CountryService countryService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final CustomMapper customMapper;
    private final PhysicalExaminationService physicalExaminationService;

    public DoctorController(CountryService countryService,
                            UserService userService,
                            UserRoleService userRoleService,
                            ModelMapper modelMapper,
                            CustomMapper customMapper,
                            PhysicalExaminationService physicalExaminationService) {

        this.countryService = countryService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.customMapper = customMapper;
        this.physicalExaminationService = physicalExaminationService;
    }

    @GetMapping("/doctor/register-patient")
    public String patientRegister(Model model) {
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("postLink", "/doctor/register-patient");

        return "user-register-form";
    }

    @PostMapping("/doctor/register-patient")
    public String patientRegisterConfirm(@Valid PatientRegisterBindingModel registerBindingModel,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerBindingModel", registerBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerBindingModel", bindingResult);
            return "redirect:/doctor/register-patient";
        }

        UserServiceModel userByPersonalCitizenNumber = this.userService.findUserByPersonalCitizenNumber(registerBindingModel.getPersonalCitizenNumber());
        if (userByPersonalCitizenNumber != null) {
            throw new ObjectAlreadyExistsException("User with personal citizen number " + userByPersonalCitizenNumber.getPersonalCitizenNumber() + " already exists.");
        }

        Long patientUserRoleId = this.userRoleService.findRoleByEnum(UserRoleEnum.PATIENT).getId();
        UserServiceModel userServiceModel = this.customMapper.mapUserBaseBindingModelToUserServiceModel(registerBindingModel, List.of(patientUserRoleId));

        UserServiceModel save = this.userService.saveToDb(userServiceModel);

        redirectAttributes.addFlashAttribute("initialUsername", save.getUsername());
        redirectAttributes.addFlashAttribute("initialPassword", save.getInitialRawPassword());

        return "redirect:/user/" + save.getId() + "/details";
    }

    @ModelAttribute("registerBindingModel")
    public PatientRegisterBindingModel patientRegisterBindingModel() {
        return new PatientRegisterBindingModel();
    }

    @GetMapping("/doctor/edit-patient-details/{id}")
    @PreAuthorize("@userServiceImpl.hasPatientRole(#id)")
    public String editPatientDetails(@PathVariable Long id, Model model) {
        UserServiceModel userById = this.userService.findUserById(id);
        PatientEditBindingModel patientEditBindingModel = this.modelMapper.map(userById, PatientEditBindingModel.class);

        model.addAttribute("userId", id);
        model.addAttribute("editBindingModel", patientEditBindingModel);
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("allRoles", this.userRoleService.findAllRoles());
        model.addAttribute("postLink", "/doctor/edit-patient-details/" + id);

        return "user-edit-form";
    }

    //TODO EDIT PATIENT POST Mapping
    //TODO EDIT PATIENT POST MAPPING VALIDATIONS

    @PatchMapping("/doctor/edit-patient-details/{id}")
    @PreAuthorize("@userServiceImpl.hasPatientRole(#id)")
    public String editPatientDetails(@PathVariable Long id, @Valid PatientEditBindingModel patientEditBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("editBindingModel", patientEditBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.editBindingModel", bindingResult);

            return "redirect:/doctor/edit-patient-details/{id}/err";
        }

        Long patientRoleId = this.userRoleService.findRoleByEnum(UserRoleEnum.PATIENT).getId();
        UserServiceModel userServiceModel = this.customMapper.mapUserBaseBindingModelToUserServiceModel(patientEditBindingModel, List.of(patientRoleId));

        this.userService.patchUser(userServiceModel, id);
        return "redirect:/user/" + id + "/details";
    }

    @GetMapping("/doctor/edit-patient-details/{id}/err")
    @PreAuthorize("@userServiceImpl.hasPatientRole(#id)")
    public String editPatientDetailsErr(@PathVariable Long id, Model model) {
        //TODO CHECK IF USER IS A PATIENT
        model.addAttribute("userId", id);
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("allRoles", this.userRoleService.findAllRoles());
        model.addAttribute("postLink", "/doctor/edit-patient-details/" + id);
        return "user-edit-form";
    }

    @GetMapping("/doctor/get-all-patients")
    public String getAllPatients(Model model) {
        model.addAttribute("link", "/api/get-all-patients");
        model.addAttribute("isAdmin", false);
        return "all-users-admin-panel";
    }

    @GetMapping("/doctor/physical-examination/info/{id}")
    public String physicalExamination(@PathVariable("id") Long physicalExaminationID, Model model) {

        PhysicalExaminationServiceModel physicalExamination = this.physicalExaminationService.findPhysicalExaminationById(physicalExaminationID);
        DoctorServiceModel doctor = physicalExamination.getDoctor();
        PatientServiceModel patient = physicalExamination.getPatient();


        UserInfoViewModel patientViewModel = this.customMapper.mapUserServiceModelToViewModel(patient.getPatientProfile());
        UserInfoViewModel doctorViewModel = this.customMapper.mapUserServiceModelToViewModel(doctor.getDoctorProfile());
        String doctorMedicalSpecialties = doctor.getSpecialties().stream()
                .map(MedicalSpecialtyServiceModel::getSpecialtyName)
                .collect(Collectors.joining(", "));

        model
                .addAttribute("patient", patientViewModel)
                .addAttribute("doctor", doctorViewModel)
                .addAttribute("medicalSpecialties", doctorMedicalSpecialties);

        return "physical-examination";
    }
}