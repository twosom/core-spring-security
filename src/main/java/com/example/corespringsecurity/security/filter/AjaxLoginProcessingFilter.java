package com.example.corespringsecurity.security.filter;

import com.example.corespringsecurity.domain.AccountDto;
import com.example.corespringsecurity.security.token.AjaxAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isAjax(request)) {
            throw new IllegalStateException("Authentication is not supports");
        }

        AccountDto accountDto = new ObjectMapper().readValue(request.getInputStream(), AccountDto.class);

        if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password must not be null");
        }

        return getAuthenticationManager()
                .authenticate(
                        new AjaxAuthenticationToken(
                                accountDto.getUsername(),
                                accountDto.getPassword()
                        )
                );
    }

    private boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }





    /* SETTER */

    public AjaxLoginProcessingFilter authManager(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        return this;
    }

    public AjaxLoginProcessingFilter successHandler(AuthenticationSuccessHandler successHandler) {
        setAuthenticationSuccessHandler(successHandler);
        return this;
    }

    public AjaxLoginProcessingFilter failureHandler(AuthenticationFailureHandler failureHandler) {
        setAuthenticationFailureHandler(failureHandler);
        return this;
    }
}
