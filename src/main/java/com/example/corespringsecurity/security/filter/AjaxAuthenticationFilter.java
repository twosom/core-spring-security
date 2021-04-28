package com.example.corespringsecurity.security.filter;

import com.example.corespringsecurity.domain.dto.AccountDto;
import com.example.corespringsecurity.security.token.AjaxAuthenticationToken;
import com.example.corespringsecurity.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public AjaxAuthenticationFilter() {
        super(new AntPathRequestMatcher("/ajaxLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
            throw new IllegalStateException("Authentication method not supported");
        }

        AccountDto accountDto = new ObjectMapper().readValue(request.getInputStream(), AccountDto.class);
        if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
            throw new AuthenticationServiceException("Username or Password is Empty");
        }

        return getAuthenticationManager()
                .authenticate(
                        new AjaxAuthenticationToken(
                                accountDto.getUsername(),
                                accountDto.getPassword()
                        )
                );
    }
}
