package com.stv.medinfosys.web.interceptor;

import com.stv.medinfosys.service.UserAccessStatsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class UserAccessStatsInterceptor implements HandlerInterceptor {
    private final UserAccessStatsService userAccessStatsService;

    public UserAccessStatsInterceptor(UserAccessStatsService userAccessStatsService) {
        this.userAccessStatsService = userAccessStatsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.userAccessStatsService.onRequest(request, response);
        return true;
    }
}
