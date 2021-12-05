package com.stv.medinfosys.service.aspect;

import com.stv.medinfosys.model.enums.UserRoleEnum;
import com.stv.medinfosys.model.service.DoctorServiceModel;
import com.stv.medinfosys.model.service.PatientServiceModel;
import com.stv.medinfosys.model.service.UserRoleServiceModel;
import com.stv.medinfosys.model.service.UserServiceModel;
import com.stv.medinfosys.service.DoctorService;
import com.stv.medinfosys.service.PatientService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class PatientAspect {
    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientAspect(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @Pointcut("execution(* com.stv.medinfosys.service.UserService.saveToDb(..))")
    public void userEntitySaved() {
    }

    @Around("userEntitySaved()")
    public Object proceedRoles(ProceedingJoinPoint pjp) throws Throwable {
        UserServiceModel registeredUser = (UserServiceModel) pjp.proceed();
        List<UserRoleEnum> roles = registeredUser.getRoles().stream()
                .map(UserRoleServiceModel::getRole)
                .collect(Collectors.toList());

        if (roles.contains(UserRoleEnum.PATIENT)) {
            this.patientService.createPatient(registeredUser);
        }

        if (roles.contains(UserRoleEnum.DOCTOR)) {
            this.doctorService.createDoctor(registeredUser);
        }

        return registeredUser;
    }

    @Pointcut("execution(* com.stv.medinfosys.service.UserService.patchUser(..))")
    public void userEntityPatch() {
    }

    @Around("userEntityPatch()")
    public Object patchRoles(ProceedingJoinPoint pjp) throws Throwable {
        UserServiceModel patchedUser = (UserServiceModel) pjp.proceed();

        List<UserRoleEnum> roles = patchedUser.getRoles().stream()
                .map(UserRoleServiceModel::getRole)
                .collect(Collectors.toList());

        if (roles.contains(UserRoleEnum.PATIENT)) {
            PatientServiceModel patientByUserPersonalCitizenNumber =
                    this.patientService.findPatientByUserPersonalCitizenNumber(patchedUser.getPersonalCitizenNumber());
            if (patientByUserPersonalCitizenNumber == null) {
                this.patientService.createPatient(patchedUser);
            }
        }

        if (roles.contains(UserRoleEnum.DOCTOR)) {
            DoctorServiceModel doctorByUserPersonalCitizenNumber
                    = this.doctorService.findDoctorByUserPersonalCitizenNumber(patchedUser.getPersonalCitizenNumber());
            if (doctorByUserPersonalCitizenNumber == null) {
                this.doctorService.createDoctor(patchedUser);
            }
        }

        return patchedUser;
    }
}
