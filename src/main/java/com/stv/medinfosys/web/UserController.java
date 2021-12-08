package com.stv.medinfosys.web;

import com.stv.medinfosys.components.CustomMapper;
import com.stv.medinfosys.model.binding.UserLoginBindingModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.UserInfoViewModel;
import com.stv.medinfosys.service.CloudinaryService;
import com.stv.medinfosys.service.CountryService;
import com.stv.medinfosys.service.UserRoleService;
import com.stv.medinfosys.service.UserService;
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

@Controller
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CountryService countryService;
    private final UserRoleService userRoleService;
    private final CloudinaryService cloudinaryService;
    private final CustomMapper customMapper;

    public UserController(ModelMapper modelMapper,
                          UserService userService,
                          CountryService countryService,
                          UserRoleService userRoleService,
                          CloudinaryService cloudinaryService, CustomMapper customMapper) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.countryService = countryService;
        this.userRoleService = userRoleService;
        this.cloudinaryService = cloudinaryService;
        this.customMapper = customMapper;
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

        //TODO SHOW ROLES
        //TODO EDIT ROLES

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