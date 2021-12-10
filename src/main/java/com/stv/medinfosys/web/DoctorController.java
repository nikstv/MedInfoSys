package com.stv.medinfosys.web;

import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.model.binding.PatientEditBindingModel;
import com.stv.medinfosys.model.binding.PatientRegisterBindingModel;
import com.stv.medinfosys.model.binding.PhysicalExaminationBindingModel;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.model.service.PhysicalExaminationServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.service.*;
import com.stv.medinfosys.utils.CustomMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class DoctorController {
    private final CountryService countryService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final CustomMapper customMapper;
    private final PhysicalExaminationService physicalExaminationService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public DoctorController(CountryService countryService,
                            UserService userService,
                            UserRoleService userRoleService,
                            ModelMapper modelMapper,
                            CustomMapper customMapper,
                            PhysicalExaminationService physicalExaminationService,
                            PatientService patientService,
                            DoctorService doctorService) {

        this.countryService = countryService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.customMapper = customMapper;
        this.physicalExaminationService = physicalExaminationService;
        this.patientService = patientService;
        this.doctorService = doctorService;
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


    @GetMapping("/doctor/physical-examination/{id}")
    public String physicalExamination(@PathVariable("id") Long userId) {
        return "physical-examination";
    }

    @PostMapping("/doctor/physical-examination/{id}")
    public String physicalExaminationConfirm(@PathVariable("id") Long userId, Principal principal, @Valid PhysicalExaminationBindingModel physicalExaminationBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("physicalExaminationBindingModel",physicalExaminationBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.physicalExaminationBindingModel", bindingResult);
            return "redirect:/doctor/physical-examination/" + userId;
        }

        PatientServiceModel patientByUserId = this.patientService.findPatientByUserId(userId);
        PhysicalExaminationServiceModel map = this.modelMapper.map(physicalExaminationBindingModel, PhysicalExaminationServiceModel.class);
        map.setPatient(patientByUserId);

        DoctorServiceModel doctorByUserUsername = this.doctorService.findDoctorByUserUsername(principal.getName());
        map.setDoctor(doctorByUserUsername);
        PhysicalExaminationServiceModel saved = this.physicalExaminationService.savePhysicalExaminationToDb(map);

        return "redirect:/user/physical-examination/info/" + saved.getId();
    }

    @ModelAttribute
    public PhysicalExaminationBindingModel physicalExaminationBindingModel() {
        return new PhysicalExaminationBindingModel();
    }
}