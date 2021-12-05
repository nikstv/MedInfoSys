package com.stv.medinfosys.config;

import com.stv.medinfosys.model.enums.UserRoleEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final SessionRegistry sessionRegistry;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, SessionRegistry sessionRegistry) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(UserRoleEnum.ADMIN.name())
                .antMatchers("/doctor/**").hasRole(UserRoleEnum.DOCTOR.name())
                .antMatchers("/", "/user/login", "/img/**", "/js/**", "/css/**").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().loginPage("/user/login").usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/").failureForwardUrl("/user/login-err")
                .and()
                .logout().logoutUrl("/user/logout").logoutSuccessUrl("/")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID");

        http.
                sessionManagement()
                .maximumSessions(-1)
                .sessionRegistry(sessionRegistry)
                .expiredUrl("/session-expired");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder);
    }
}