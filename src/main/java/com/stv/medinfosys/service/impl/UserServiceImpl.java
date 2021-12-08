package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.exception.ObjectNotFoundException;
import com.stv.medinfosys.model.entity.CloudinaryPictureEntity;
import com.stv.medinfosys.model.entity.CountryEntity;
import com.stv.medinfosys.model.entity.UserEntity;
import com.stv.medinfosys.model.entity.UserRoleEntity;
import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.UserRoleServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.ActiveUserCountViewModel;
import com.stv.medinfosys.repository.UserRepository;
import com.stv.medinfosys.service.CloudinaryService;
import com.stv.medinfosys.service.UserRoleService;
import com.stv.medinfosys.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final SessionRegistry sessionRegistry;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService, SessionRegistry sessionRegistry) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        Optional<UserEntity> userOpt = this.userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }

        UserEntity userEntity = userOpt.get();
        return modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public UserServiceModel saveToDb(UserServiceModel userServiceModel) {
        UserEntity userEntity = this.modelMapper.map(userServiceModel, UserEntity.class);

        String initialRawPassword = "";
        if (userEntity.getUsername() == null || userEntity.getPassword() == null ||
                userEntity.getUsername().trim().equals("") || userEntity.getPassword().trim().equals("")) {
            userEntity.setUsername(UUID.randomUUID().toString());
            initialRawPassword = UUID.randomUUID().toString();
            userEntity.setPassword(passwordEncoder.encode(initialRawPassword));
        } else {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }

        Type listOfUserRoleEntityType = new TypeToken<List<UserRoleEntity>>() {
        }.getType();
        userEntity.setRoles(this.modelMapper.map(userServiceModel.getRoles(), listOfUserRoleEntityType));

        UserEntity save = this.userRepository.save(userEntity);
        UserServiceModel result = modelMapper.map(save, UserServiceModel.class);
        result.setInitialRawPassword(initialRawPassword);
        return result;
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        List<UserEntity> all = this.userRepository.findAll();
        Type userServiceModelTypeToken = new TypeToken<List<UserServiceModel>>() {
        }.getType();

        return modelMapper.map(all, userServiceModelTypeToken);
    }

    @Override
    public UserServiceModel findUserById(Long id) {
        Optional<UserEntity> byId = this.userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " not found.");
        }

        return this.modelMapper.map(byId.get(), UserServiceModel.class);
    }

    @Override
    @Transactional
    public UserServiceModel patchUser(UserServiceModel updated, Long userToPatchId) {
        Optional<UserEntity> toPatchOpt = this.userRepository.findById(userToPatchId);
        if (toPatchOpt.isEmpty()) {
            throw new IllegalArgumentException("User with id " + userToPatchId + " not found.");
        }

        List<UserRoleServiceModel> userRoleServiceModels = updated.getRoles();
        Type listOfuserRoleEntityType = new TypeToken<List<UserRoleEntity>>() {
        }.getType();
        List<UserRoleEntity> mappedRoles = this.modelMapper.map(userRoleServiceModels, listOfuserRoleEntityType);

        UserEntity userEntity = toPatchOpt.get();
        userEntity
                .setFirstName(updated.getFirstName())
                .setMiddleName(updated.getMiddleName())
                .setLastName(updated.getLastName())
                .setPersonalCitizenNumber(updated.getPersonalCitizenNumber())
                .setIdentityDocNumber(updated.getIdentityDocNumber())
                .setTelNumber(updated.getTelNumber())
                .setCountry(this.modelMapper.map(updated.getCountry(), CountryEntity.class))
                .setState(updated.getState())
                .setMunicipality(updated.getMunicipality())
                .setCity(updated.getCity())
                .setDistrict(updated.getDistrict())
                .setStreet(updated.getStreet())
                .setNumber(updated.getNumber())
                .setRoles(mappedRoles)
                .setAdditionalInfo(updated.getAdditionalInfo());

        if (updated.getPicture() != null) {
            if (userEntity.getPicture() != null) {
                this.cloudinaryService.deleteImage(userEntity.getPicture().getPublicId());
            }

            userEntity.setPicture(this.modelMapper.map(updated.getPicture(), CloudinaryPictureEntity.class));
        }

        UserEntity save = this.userRepository.save(userEntity);
        String username = save.getUsername();

        this.expireSessionNow(username);

        return modelMapper.map(save, UserServiceModel.class);
    }

    @Override
    public void expireSessionNow(String username) {
        List<SessionInformation> allSessions =
                this.sessionRegistry.getAllSessions(new User(username, "", List.of()), true);
        for (SessionInformation session : allSessions) {
            session.expireNow();
        }
    }

    @Override
    public String generateNewUserPassword(Long id) {
        Optional<UserEntity> byId = this.userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("User with id " + id + " not found.");
        }

        UserEntity userEntity = byId.get();
        String newPassword = UUID.randomUUID().toString();
        userEntity
                .setPassword(passwordEncoder.encode(newPassword));
        UserEntity save = this.userRepository.save(userEntity);
        return newPassword;
    }

    //TODO DELETE
    @Override
    public UserServiceModel findUserByPersonalCitizenNumber(String personalCitizenNumber) {
        Optional<UserEntity> byPersonalCitizenNumber = this.userRepository.findByPersonalCitizenNumber(personalCitizenNumber);
        if (byPersonalCitizenNumber.isEmpty()) {
            return null;
        }

        return this.modelMapper.map(byPersonalCitizenNumber.get(), UserServiceModel.class);
    }

    @Override
    public boolean canViewUserDetails(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean principalIsAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRoleEnum.ADMIN));
        if (principalIsAdmin) {
            return true;
        }

        Optional<UserEntity> byId = this.userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("User with id: " + userId + " was not found");
        }

        if (byId.get().getUsername().equals(authentication.getName())) {
            return true;
        }

        boolean principalIsDoctor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + UserRoleEnum.DOCTOR));
        boolean userIsPatient = byId.get().getRoles().stream()
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toList())
                .contains(UserRoleEnum.PATIENT);

        if (userIsPatient && principalIsDoctor) {
            return true;
        }

        return false;
    }

    @Override
    public boolean noUsersInDb() {
        return this.userRepository.count() == 0;
    }

    @Override
    public boolean hasPatientRole(Long userId) {
        Optional<UserEntity> byIdOpt = this.userRepository.findById(userId);
        if (byIdOpt.isEmpty()) {
            throw new ObjectNotFoundException("User with id " + userId + " was not found");
        }

        return byIdOpt.get().getRoles().stream()
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toList())
                .contains(UserRoleEnum.PATIENT);
    }

    @Override
    public List<UserServiceModel> findAllPatients() {
        List<UserEntity> allPatients = this.userRepository.findAllPatients();
        Type type = new TypeToken<List<UserServiceModel>>() {
        }.getType();

        return this.modelMapper.map(allPatients, type);
    }

    @Override
    public ActiveUserCountViewModel getCountOfActiveUsers() {
        int size = this.sessionRegistry.getAllPrincipals().size();
        return new ActiveUserCountViewModel(size);
    }

    @Override
    public void markForDelete(Long userId) {
        Optional<UserEntity> byId = this.userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("User with id " + userId + " was not found");
        }

        UserEntity userEntity = byId.get();
        userEntity.setEnabled(false);
        this.userRepository.save(userEntity);
        this.expireSessionNow(userEntity.getUsername());
    }

    @Override
    public void lockAccountToogle(Long userId) {
        Optional<UserEntity> byId = this.userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new ObjectNotFoundException("User with id " + userId + " was not found");
        }

        UserEntity userEntity = byId.get();

        if (userEntity.getAccountNonLocked()==true){
            userEntity.setAccountNonLocked(false);
        } else if (userEntity.getAccountNonLocked()==false){
            userEntity.setAccountNonLocked(true);
        }

        this.userRepository.save(userEntity);
        this.expireSessionNow(userEntity.getUsername());
    }
}