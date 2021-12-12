package com.stv.medinfosys.web;

import com.stv.medinfosys.model.binding.ChangeUsernameAndPasswordBindingModel;
import com.stv.medinfosys.model.service.*;
import com.stv.medinfosys.model.view.PhysicalExaminationViewModel;
import com.stv.medinfosys.service.*;
import com.stv.medinfosys.utils.CustomMapper;
import com.stv.medinfosys.model.binding.UserLoginBindingModel;
import com.stv.medinfosys.model.view.UserInfoViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final CustomMapper customMapper;
    private final DoctorService doctorService;
    private final PhysicalExaminationService physicalExaminationService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService,
                          CustomMapper customMapper,
                          DoctorService doctorService,
                          PhysicalExaminationService physicalExaminationService,
                          ModelMapper modelMapper) {

        this.userService = userService;
        this.customMapper = customMapper;
        this.doctorService = doctorService;
        this.physicalExaminationService = physicalExaminationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/user/login")
    public String login(Model model) {
        if (!model.containsAttribute("invalidLogin")) {
            model.addAttribute("invalidLogin", false);
        }
        return "user-login-form";
    }

    @PostMapping("/user/login-err")
    public String loginErr(UserLoginBindingModel userLoginBindingModel, RedirectAttributes redirectAttributes) {
        userLoginBindingModel.setPassword(null);
        redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
        redirectAttributes.addFlashAttribute("invalidLogin", true);
        return "redirect:/user/login";
    }

    @ModelAttribute
    public UserLoginBindingModel userLoginBindingModel() {
        return new UserLoginBindingModel();
    }

    @GetMapping("/user/{id}/details")
    @PreAuthorize("@userServiceImpl.canViewUserDetails(#id)")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("initialPassword") && !model.containsAttribute("initialUsername")) {
            model.addAttribute("initialPassword", null);
            model.addAttribute("initialUsername", null);
        }

        UserServiceModel userByIdServiceModel = this.userService.findUserById(id);
        UserInfoViewModel userInfoViewModel = this.customMapper.mapUserServiceModelToViewModel(userByIdServiceModel);
        model.addAttribute("userInfoViewModel", userInfoViewModel);

        model.addAttribute("isDoctor", false);
        boolean isDoctor = this.userService.hasDoctorRole(userByIdServiceModel.getId());
        if (isDoctor) {
            String specialties = this.doctorService.findDoctorProfileByUserId(userByIdServiceModel.getId())
                    .getSpecialties()
                    .stream()
                    .map(MedicalSpecialtyServiceModel::getSpecialtyName)
                    .collect(Collectors.joining(", "));
            model.addAttribute("specialties", specialties);
            model.addAttribute("isDoctor", true);
        }

        boolean isPatient = this.userService.hasPatientRole(userByIdServiceModel.getId());
        if (isPatient) {
            List<PhysicalExaminationServiceModel> allPhysicalExaminationsByPatientId
                    = this.physicalExaminationService
                    .findAllPhysicalExaminationsByUserId(userByIdServiceModel.getId());

            List<PhysicalExaminationViewModel> physicalExaminationViewModels
                    = allPhysicalExaminationsByPatientId.stream()
                    .map(customMapper::mapPhysicalExaminationServiceToViewModel)
                    .collect(Collectors.toList());

            model.addAttribute("physicalExaminations", physicalExaminationViewModels);
        }

        if(this.userService.canChangeLoginCredentials(id)){
            model.addAttribute("canChangeLoginCredentials", true);
        }

        return "user-details-view";
    }

    @GetMapping("/user/my-profile")
    @PreAuthorize("isAuthenticated()")
    public String loggedUserProfile(Principal principal) {
        UserServiceModel byUsername = this.userService.findByUsername(principal.getName());
        return "redirect:/user/" + byUsername.getId() + "/details";
    }

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "session-expired";
    }

    @GetMapping("/user/physical-examination/info/{id}")
    @PreAuthorize("@userServiceImpl.canViewPhysicalExaminationDetails(#physicalExaminationID)")
    public String physicalExaminationInfo(@PathVariable("id") Long physicalExaminationID, Model model) {

        PhysicalExaminationServiceModel physicalExamination = this.physicalExaminationService.findPhysicalExaminationById(physicalExaminationID);
        PhysicalExaminationViewModel physicalExaminationViewModel = this.modelMapper.map(physicalExamination, PhysicalExaminationViewModel.class);

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
                .addAttribute("medicalSpecialties", doctorMedicalSpecialties)
                .addAttribute("physicalExamination", physicalExaminationViewModel);

        return "physical-examination-info";
    }

    @GetMapping("/user/change-login-credentials/{id}")
    @PreAuthorize("@userServiceImpl.canChangeLoginCredentials(#id)")
    public String changeLoginCredentials(@PathVariable Long id) {

        return "change-login-credentials";
    }

    @ModelAttribute("changeUsernameAndPasswordBindingModel")
    ChangeUsernameAndPasswordBindingModel changeUsernameAndPasswordBindingModel() {
        return new ChangeUsernameAndPasswordBindingModel();
    }

    @PatchMapping("/user/change-login-credentials/{id}")
    @PreAuthorize("@userServiceImpl.canChangeLoginCredentials(#id)")
    public String changeLoginCredentialsConfirm(@PathVariable Long id,
                                                @Valid ChangeUsernameAndPasswordBindingModel changeUsernameAndPasswordBindingModel,
                                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        boolean passwordsMatch = changeUsernameAndPasswordBindingModel.getPassword().equals(changeUsernameAndPasswordBindingModel.getConfirmPassword());
        if (!passwordsMatch) {
            redirectAttributes
                    .addFlashAttribute("customMessage", "Entered passwords do not match")
                    .addFlashAttribute("changeUsernameAndPasswordBindingModel", changeUsernameAndPasswordBindingModel)
                    .addFlashAttribute("fieldsNotMatch", true);
            return "redirect:/user/change-login-credentials/" + id;
        }

        boolean usernamesMatch = changeUsernameAndPasswordBindingModel.getUsername().equals(changeUsernameAndPasswordBindingModel.getConfirmUsername());
        if (!usernamesMatch) {
            redirectAttributes
                    .addFlashAttribute("customMessage", "Entered usernames do not match")
                    .addFlashAttribute("changeUsernameAndPasswordBindingModel", changeUsernameAndPasswordBindingModel)
                    .addFlashAttribute("fieldsNotMatch", true);
            return "redirect:/user/change-login-credentials/" + id;
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("changeUsernameAndPasswordBindingModel", changeUsernameAndPasswordBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.changeUsernameAndPasswordBindingModel", bindingResult);
            return "redirect:/user/change-login-credentials/" + id;
        }

        this.userService.changeLoginCredentialsByUser(
                changeUsernameAndPasswordBindingModel.getUsername(),
                changeUsernameAndPasswordBindingModel.getPassword(),
                id
        );

        return "redirect:/user/" + id + "/details";
    }
}