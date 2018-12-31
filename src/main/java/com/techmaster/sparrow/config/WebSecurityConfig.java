package com.techmaster.sparrow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired AuthFailedHandler authFailedHandler;
    @Autowired AuthSuccessHandler authSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/home", "/api/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .successHandler(authSuccessHandler)
            .failureHandler(authFailedHandler)
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService service = new UserDetailsServiceImpl();
        auth.userDetailsService(service).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}
