package com.stv.medinfosys.web;

import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.AdminPanelUserViewModel;
import com.stv.medinfosys.repository.UserRepository;
import com.stv.medinfosys.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
public class MedInfoSysRestController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public MedInfoSysRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api/get-all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminPanelUserViewModel>> getAllUsers(){
        List<UserServiceModel> allUsers = this.userService.findAllUsers();

        Type type = new TypeToken<List<AdminPanelUserViewModel>>() {
        }.getType();
        List<AdminPanelUserViewModel> map = this.modelMapper.map(allUsers, type);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/api/get-all-patients")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AdminPanelUserViewModel>> getAllPatients(){
        List<UserServiceModel> allPatients = this.userService.findAllPatients();

        Type type = new TypeToken<List<AdminPanelUserViewModel>>() {
        }.getType();
        List<AdminPanelUserViewModel> map = this.modelMapper.map(allPatients, type);

        return ResponseEntity.ok(map);
    }
}
