package com.example.corespringsecurity.security.handler.ajax;

import com.example.corespringsecurity.domain.Account;
import com.example.corespringsecurity.domain.AccountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final ModelMapper modelMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        Account account = (Account) authentication.getPrincipal();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        new ObjectMapper().writeValue(response.getWriter(), accountDto);
    }
}
