package com.stv.medinfosys.service;

import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.model.view.ActiveUserCountViewModel;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserServiceModel findByUsername(String username);

    UserServiceModel saveToDb(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    UserServiceModel findUserById(Long id);

    UserServiceModel patchUser(UserServiceModel updated, Long userToPatchId);

    void expireSessionNow(String username);

    String generateNewUserPassword(Long id);

    UserServiceModel findUserByPersonalCitizenNumber(String personalCitizenNumber); //TODO DELETE

    boolean canViewUserDetails(Long userId);

    boolean noUsersInDb();

    boolean hasPatientRole(Long userId);

    List<UserServiceModel> findAllPatients();

    ActiveUserCountViewModel getCountOfActiveUsers();

    void disableAccount(Long userId);

    void lockAccount(Long userId);
}
