package com.stv.medinfosys.web;

import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.MedicalSpecialtyServiceModel;
import com.stv.medinfosys.model.service.UserRoleServiceModel;
import com.stv.medinfosys.service.*;
import com.stv.medinfosys.utils.CustomMapper;
import com.stv.medinfosys.model.binding.UserLoginBindingModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.UserInfoViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CountryService countryService;
    private final UserRoleService userRoleService;
    private final CloudinaryService cloudinaryService;
    private final CustomMapper customMapper;
    private final DoctorService doctorService;

    public UserController(ModelMapper modelMapper,
                          UserService userService,
                          CountryService countryService,
                          UserRoleService userRoleService,
                          CloudinaryService cloudinaryService,
                          CustomMapper customMapper,
                          DoctorService doctorService) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.countryService = countryService;
        this.userRoleService = userRoleService;
        this.cloudinaryService = cloudinaryService;
        this.customMapper = customMapper;
        this.doctorService = doctorService;
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
        boolean isDoctor = userByIdServiceModel.getRoles().stream()
                .map(UserRoleServiceModel::getRole)
                .collect(Collectors.toList())
                .contains(UserRoleEnum.DOCTOR);


        if (isDoctor){
            String specialties = this.doctorService.findDoctorProfileByUserId(userByIdServiceModel.getId())
                    .getSpecialties()
                    .stream()
                    .map(MedicalSpecialtyServiceModel::getSpecialtyName)
                    .collect(Collectors.joining(", "));
            model.addAttribute("specialties", specialties);
            model.addAttribute("isDoctor", true);
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
    public String sessionExpired(){
        return "session-expired";
    }
}