package com.example.corespringsecurity.security.configs;

import com.example.corespringsecurity.security.service.CustomUserDetails1;
import com.example.corespringsecurity.security.service.CustomUserDetails2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@Order(0)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;
    private final CustomUserDetails1 customUserDetails1;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/mypage").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers("/messages").hasAnyRole("MANAGER", "ADMIN")
//                .antMatchers("/config").hasRole("ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .formLogin();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetails1)
                .passwordEncoder(encoder);
    }
}

@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@Order(1)
class SecurityConfig2 extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder encoder;
    private final CustomUserDetails2 customUserDetails2;

    @Override

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/config").hasRole("ADMIN")
                .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetails2)
                .passwordEncoder(encoder);

    }
}
