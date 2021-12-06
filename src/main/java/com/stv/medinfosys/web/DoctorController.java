package com.stv.medinfosys.web;

import com.stv.medinfosys.components.CustomMapper;
import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.model.binding.PatientEditBindingModel;
import com.stv.medinfosys.model.binding.PatientRegisterBindingModel;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.AdminPanelUserViewModel;
import com.stv.medinfosys.service.CountryService;
import com.stv.medinfosys.service.PatientService;
import com.stv.medinfosys.service.UserRoleService;
import com.stv.medinfosys.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DoctorController {
    private final CountryService countryService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final CustomMapper customMapper;
    private final PatientService patientService;

    public DoctorController(CountryService countryService,
                            UserService userService,
                            UserRoleService userRoleService,
                            ModelMapper modelMapper,
                            CustomMapper customMapper,
                            PatientService patientService) {

        this.countryService = countryService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.customMapper = customMapper;
        this.patientService = patientService;
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
        //TODO CHECK IF USER IS A PATIENT
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
        return "all-users-admin-panel";
    }
}