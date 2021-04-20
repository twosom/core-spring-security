package com.example.corespringsecurity.security.handler;

import com.example.corespringsecurity.kafka.producer.KafkaProducer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Component

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final KafkaProducer kafkaProducer;

    public CustomAuthenticationSuccessHandler(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String jwt = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS512, "SOMANG")
                .compact();

        kafkaProducer.sendJwtToken("jwt", jwt);


        setDefaultTargetUrl("/");

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        redirectStrategy
                .sendRedirect(
                        request, response,
                        savedRequest != null ? savedRequest.getRedirectUrl() : getDefaultTargetUrl()
                );

    }
}
