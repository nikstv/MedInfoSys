package com.stv.medinfosys.service.impl;

import com.stv.medinfosys.model.service.UserAccessStatsServiceModel;
import com.stv.medinfosys.service.UserAccessStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserAccessStatsServiceImpl implements UserAccessStatsService {

    Logger logger = LoggerFactory.getLogger(UserAccessStatsService.class);

    @Override
    public void onRequest(HttpServletRequest request, HttpServletResponse response) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        int responseStatus = response.getStatus();

        UserAccessStatsServiceModel userAccessStatsServiceModel = new UserAccessStatsServiceModel(username, method, requestURI, responseStatus);
        logger.info(userAccessStatsServiceModel.toString());
    }
}
