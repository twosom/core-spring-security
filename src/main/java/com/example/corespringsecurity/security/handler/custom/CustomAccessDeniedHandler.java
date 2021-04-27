package com.example.corespringsecurity.security.handler.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter @Setter
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage = "/denied";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String authorize;

        switch (request.getRequestURI()) {
            case "/messages":
                authorize = "MANAGER";
                break;
            case "/config":
                authorize = "ADMIN";
                break;
            default:
                authorize = "USER";
                break;
        }


        String deniedUrl = errorPage + "?exception=" + accessDeniedException.getMessage() + "&authorize=" + authorize;
        response.sendRedirect(deniedUrl);
    }

}
