package com.example.corespringsecurity.security.common;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

@Getter
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private String secretKey;

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.secretKey = request.getParameter("secret_key");
    }
}
