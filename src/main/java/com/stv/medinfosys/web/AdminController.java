package com.stv.medinfosys.web;

import com.stv.medinfosys.model.service.BaseServiceModel;
import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.service.*;
import com.stv.medinfosys.utils.CustomMapper;
import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.model.binding.UserEditBindingModel;
import com.stv.medinfosys.model.binding.UserRegisterBindingModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRoleService userRoleService;
    private final CountryService countryService;
    private final CustomMapper customMapper;
    private final MedicalSpecialtyService medicalSpecialtyService;
    private final DoctorService doctorService;

    public AdminController(UserService userService,
                           ModelMapper modelMapper,
                           UserRoleService userRoleService,
                           CountryService countryService,
                           CustomMapper customMapper,
                           MedicalSpecialtyService medicalSpecialtyService,
                           DoctorService doctorService) {

        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRoleService = userRoleService;
        this.countryService = countryService;
        this.customMapper = customMapper;
        this.medicalSpecialtyService = medicalSpecialtyService;
        this.doctorService = doctorService;
    }

    @GetMapping("/admin/users")
    public String getUsersEditList(Model model) {
        model.addAttribute("link", "/api/get-all-users");
        model.addAttribute("isAdmin", true);
        return "all-users-admin-panel";
    }

    @PatchMapping("/admin/user/reset-login-details")
    public String changeUserCredentials(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        String newPassword = this.userService.generateNewUserPassword(id);
        String username = this.userService.findUserById(id).getUsername();
        redirectAttributes
                .addFlashAttribute("initialPassword", newPassword)
                .addFlashAttribute("initialUsername", username);
        return "redirect:/user/" + id + "/details";
    }

    @GetMapping("/admin/user/register")
    public String userRegister(Model model) {
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("userRoles", this.userRoleService.findAllRoles());
        model.addAttribute("postLink", "/admin/user/register");

        return "user-register-form";
    }

    @ModelAttribute("registerBindingModel")
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @PostMapping("/admin/user/register")
    public String userRegisterConfirm(@Valid UserRegisterBindingModel registerBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerBindingModel", registerBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerBindingModel", bindingResult);
            return "redirect:/admin/user/register";
        }

        UserServiceModel userByPersonalCitizenNumber = this.userService.findUserByPersonalCitizenNumber(registerBindingModel.getPersonalCitizenNumber());
        if (userByPersonalCitizenNumber != null) {
            throw new ObjectAlreadyExistsException("User with personal citizen number " + userByPersonalCitizenNumber.getPersonalCitizenNumber() + " already exists.");
        }

        UserServiceModel userServiceModel = this.customMapper.mapUserBaseBindingModelToUserServiceModel(registerBindingModel, registerBindingModel.getRoles());

        UserServiceModel saved = this.userService.saveToDb(userServiceModel);

        redirectAttributes.addFlashAttribute("initialUsername", saved.getUsername());
        redirectAttributes.addFlashAttribute("initialPassword", saved.getInitialRawPassword());

        return "redirect:/user/" + saved.getId() + "/details";
    }

    @GetMapping("/admin/user/{id}/edit")
    public String editUserDetails(Model model, @PathVariable Long id) {
        UserServiceModel userServiceModel = this.userService.findUserById(id);
        List<Long> roleIds = userServiceModel.getRoles().stream()
                .map(role -> role.getId()).collect(Collectors.toList());
        UserEditBindingModel userEditBindingModel = this.modelMapper.map(userServiceModel, UserEditBindingModel.class);
        userEditBindingModel.setRolesId(roleIds);

        //TODO CHECK IF USER EXISTS IN DB. IF NOT, THROW AN ERROR.

        model.addAttribute("userId", id);
        model.addAttribute("editBindingModel", userEditBindingModel);
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("allRoles", this.userRoleService.findAllRoles());
        model.addAttribute("postLink", "/admin/user/" + id + "/edit");

        return "user-edit-form";
    }

    @PatchMapping("/admin/user/{id}/edit")
    public String editUserDetailsConfirm(@PathVariable Long id, @Valid UserEditBindingModel userEditBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("editBindingModel", userEditBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.editBindingModel", bindingResult);

            return "redirect:/admin/user/" + id + "/edit-err";
        }

        //TODO CHECK IF PERSONAL CITIZEN NUMBER IS ALREADY IN DB

        UserServiceModel userServiceModel = this.customMapper.mapUserBaseBindingModelToUserServiceModel(userEditBindingModel, userEditBindingModel.getRolesId());

        this.userService.patchUser(userServiceModel, id);
        return "redirect:/user/" + id + "/details";
    }

    @GetMapping("/admin/user/{id}/edit-err")
    public String editUserDetailsErr(Model model, @PathVariable Long id) {

        model.addAttribute("userId", id);
        model.addAttribute("allCountries", this.countryService.findAllCountries());
        model.addAttribute("allRoles", this.userRoleService.findAllRoles());
        model.addAttribute("postLink", "/admin/user/" + id + "/edit");

        return "user-edit-form";
    }

    @PostMapping("/admin/user/{id}/invalidate-session")
    public String expireSessionNow(@PathVariable Long id) {
        this.userService.expireSessionNow(this.userService.findUserById(id).getUsername());
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/user/{id}/delete-account")
    public String markAccountForDeletion(@PathVariable Long id) {
        this.userService.markForDelete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/user/{id}/lock-account-toogle")
    public String lockAccount(@PathVariable Long id) {
        this.userService.lockAccountToogle(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/edit-doctor-specialties/{id}")
    public String editDoctorMedicalSpecialties(@PathVariable Long id, Model model) {

        DoctorServiceModel doctorProfileByUserId = this.doctorService.findDoctorProfileByUserId(id);
        List<Long> specialtiesView = doctorProfileByUserId.getSpecialties().stream()
                .map(BaseServiceModel::getId)
                .collect(Collectors.toList());

        model.addAttribute("allSpecialties", this.medicalSpecialtyService.getAllMedicalSpecialties());
        model.addAttribute("specialtiesView", specialtiesView);

        return "doctor-medical-specialties-edit";
    }

    @PatchMapping("/admin/edit-doctor-specialties/{id}")
    public String editDoctorMedicalSpecialtiesConfirm(@PathVariable("id") Long userId, @RequestParam(name = "specialties", required = false) List<Long> specialtiesIds) {
        if (specialtiesIds == null) {
            specialtiesIds = new ArrayList<>();
        }

        Long doctorId = this.doctorService.findDoctorProfileByUserId(userId).getId();
        this.doctorService.patchDoctorMedicalSpecialties(doctorId, specialtiesIds);
        return "redirect:/user/" + userId + "/details";
    }
}
