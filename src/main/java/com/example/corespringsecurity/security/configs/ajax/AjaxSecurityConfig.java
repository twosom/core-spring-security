package com.example.corespringsecurity.security.configs.ajax;

import com.example.corespringsecurity.provider.AjaxAuthenticationProvider;
import com.example.corespringsecurity.security.common.ajax.AjaxAccessDeniedHandler;
import com.example.corespringsecurity.security.common.ajax.AjaxLoginAuthenticationEntryPoint;
import com.example.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import com.example.corespringsecurity.security.handler.ajax.AjaxAuthenticationFailureHandler;
import com.example.corespringsecurity.security.handler.ajax.AjaxAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Order(0)
@RequiredArgsConstructor
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider ajaxAuthProv;

    /* Exception Handler */
    private final AjaxAuthenticationSuccessHandler authenticationSuccessHandler;
    private final AjaxAuthenticationFailureHandler authenticationFailureHandler;
    private final AjaxLoginAuthenticationEntryPoint entryPoint;
    private final AjaxAccessDeniedHandler deniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated();
//            .and()
//                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.csrf().disable();

        /* Handler Setting */
        http.exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(deniedHandler);

        customConfigureAjax(http);
    }

    private void customConfigureAjax(HttpSecurity http) throws Exception {
        http.apply(new AjaxLoginConfigurer<>())
                .setAuthenticationManager(authenticationManager())
                .successHandlerAjax(authenticationSuccessHandler)
                .failureHandlerAjax(authenticationFailureHandler)
                .loginProcessingUrl("/api/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthProv);
    }

//    @Bean
//    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
//        return new AjaxLoginProcessingFilter()
//                .authManager(authenticationManager())
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler);
//    }
}
