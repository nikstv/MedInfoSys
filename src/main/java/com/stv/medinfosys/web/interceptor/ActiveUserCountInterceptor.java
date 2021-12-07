package com.stv.medinfosys.web.interceptor;

import com.stv.medinfosys.model.view.ActiveUserCountViewModel;
import com.stv.medinfosys.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Configuration
public class ActiveUserCountInterceptor implements HandlerInterceptor {
    private final UserService userService;

    public ActiveUserCountInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ActiveUserCountViewModel countOfActiveUsers = this.userService.getCountOfActiveUsers();
        if (modelAndView != null) {
            if (countOfActiveUsers != null) {
                modelAndView.addObject("countOfActiveUsers", countOfActiveUsers);
            }
        }
    }
}
