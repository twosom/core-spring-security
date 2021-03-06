package com.example.corespringsecurity.security.configs;

import com.example.corespringsecurity.provider.CustomAuthenticationProvider;
import com.example.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import com.example.corespringsecurity.security.handler.CustomAccessDeniedHandler;
import com.example.corespringsecurity.security.handler.CustomAuthenticationFailureHandler;
import com.example.corespringsecurity.security.handler.CustomAuthenticationSuccessHandler;
import com.example.corespringsecurity.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@Slf4j @RequiredArgsConstructor
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final DataSource dataSource;
    private final FormAuthenticationDetailsSource authenticationDetailsSource;

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                                                        /* /login ?????? ?????? *??? ?????? ?????? login ????????? ?????? ???????????? ??? ?????? ??????   */
                .antMatchers("/", "/users", "user/login/**", "/login*", "/api/**").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource)
                .defaultSuccessUrl("/")
                /* successHandler ??? defaultSuccessUrl ?????? ????????? ??????. */
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                /* ????????? ?????????, ????????? ?????? ?????????, ????????? ?????? ??? ???????????? permitAll ??? ????????? ???????????? ?????? ???????????? ?????? ?????? */
                .permitAll()
        .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
        .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(customUserDetailsService)
                .tokenRepository(tokenRepository());

//                http.csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* ?????? CustomAuthenticationProvider ?????? ???????????? ???????????? ?????? ?????? X */
//        auth.userDetailsService(customUserDetailsService)
//                .passwordEncoder(encoder);

        auth.authenticationProvider(authenticationProvider);
    }
}
