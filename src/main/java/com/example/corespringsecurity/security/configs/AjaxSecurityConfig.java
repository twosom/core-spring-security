package com.example.corespringsecurity.security.configs;

import com.example.corespringsecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import com.example.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import com.example.corespringsecurity.provider.AjaxAuthenticationProvider;
import com.example.corespringsecurity.security.handler.AjaxAccessDeniedHandler;
import com.example.corespringsecurity.security.handler.AjaxAuthenticationFailureHandler;
import com.example.corespringsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import com.example.corespringsecurity.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@Order(0)
@RequiredArgsConstructor
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;

    private final AjaxAuthenticationSuccessHandler successHandler;
    private final AjaxAuthenticationFailureHandler failureHandler;

    private final AjaxAccessDeniedHandler accessDeniedHandler;
    private final AjaxLoginAuthenticationEntryPoint authenticationEntryPoint;


    private final DataSource dataSource;
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/messages").hasRole("MANAGER")
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated()
        .and()
//                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()    /* ExceptionTranslationFilter 에서 예외가 발생하면 처리할 핸들링 클래스 설정 */
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http
                .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService())
                .tokenRepository(tokenRepository());

//        http.csrf().disable();

        customConfigureAjax(http);
    }

    private void customConfigureAjax(HttpSecurity http) throws Exception {
        http.apply(new AjaxLoginConfigurer<>())
                .successHandlerAjax(successHandler)
                .failureHandlerAjax(failureHandler)
                .setAuthenticationManager(authenticationManager())
                .loginProcessingUrl("/api/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider);
    }


//    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
//        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter();
//
//        filter.setAuthenticationManager(authenticationManager());
//        /* Ajax 용으로 새로 만든 Success/Failure Handler 설정 */
//        filter.setAuthenticationSuccessHandler(successHandler);
//        filter.setAuthenticationFailureHandler(failureHandler);
//
//        return filter;
//    }
}
