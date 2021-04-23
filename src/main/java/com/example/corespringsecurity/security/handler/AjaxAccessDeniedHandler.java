package com.example.corespringsecurity.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String authorize;

        switch (request.getRequestURI()) {
            case "/api/messages":
                authorize = "MANAGER";
                break;
            case "/api/config":
                authorize = "ADMIN";
                break;
            default:
                authorize = "USER";
                break;
        }
        response.addHeader("authorize", authorize);

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is denied");
    }
}
