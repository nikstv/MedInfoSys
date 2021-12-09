package com.stv.medinfosys.Schedules;

import com.stv.medinfosys.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnonymizeUsersMarkedForDelete {
    private final UserService userService;

    public AnonymizeUsersMarkedForDelete(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "1 1 3 * * *")
    public void anonymize() {
        this.userService.anonymizeAllMarkedForDeleteUsers();
    }
}
