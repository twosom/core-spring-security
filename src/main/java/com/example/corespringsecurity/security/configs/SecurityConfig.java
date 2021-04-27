package com.example.corespringsecurity.security.configs;

import com.example.corespringsecurity.provider.CustomAuthenticationProvider;
import com.example.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import com.example.corespringsecurity.security.handler.custom.CustomAccessDeniedHandler;
import com.example.corespringsecurity.security.handler.custom.CustomAuthenticationFailureHandler;
import com.example.corespringsecurity.security.handler.custom.CustomAuthenticationSuccessHandler;
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
@Order(1)
@Slf4j @RequiredArgsConstructor
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
                                                        /* /login 경로 뒤에 *를 붙여 줘야 login 경로에 받는 파라미터 값 인식 가능   */
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
                /* successHandler 는 defaultSuccessUrl 밑에 있어야 한다. */
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                /* 로그인 페이지, 로그인 처리 페이지, 로그인 성공 후 페이지는 permitAll 을 주어서 인증되지 않은 사용자도 접근 허용 */
                .permitAll()
        .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
        .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(customUserDetailsService)
                .tokenRepository(tokenRepository());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* 이미 CustomAuthenticationProvider 에서 사용하고 있으므로 굳이 추가 X */
//        auth.userDetailsService(customUserDetailsService)
//                .passwordEncoder(encoder);

        auth.authenticationProvider(authenticationProvider);
    }


}
