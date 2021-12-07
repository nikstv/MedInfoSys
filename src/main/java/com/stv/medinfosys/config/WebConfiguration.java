package com.stv.medinfosys.config;

import com.stv.medinfosys.web.interceptor.ActiveUserCountInterceptor;
import com.stv.medinfosys.web.interceptor.UserAccessStatsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final UserAccessStatsInterceptor userAccessStatsInterceptor;
    private final ActiveUserCountInterceptor activeUserCountInterceptor;

    public WebConfiguration(UserAccessStatsInterceptor userAccessStatsInterceptor, ActiveUserCountInterceptor activeUserCountInterceptor) {
        this.userAccessStatsInterceptor = userAccessStatsInterceptor;
        this.activeUserCountInterceptor = activeUserCountInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAccessStatsInterceptor);
        registry.addInterceptor(activeUserCountInterceptor);
    }
}
