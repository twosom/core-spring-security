package com.example.corespringsecurity.provider;

import com.example.corespringsecurity.security.common.FormWebAuthenticationDetails;
import com.example.corespringsecurity.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        /* 이게 과연 CustomUserDetailsService 클래스의 메소드로 호출되는지 확인 */
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        if (!encoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }


        String secretKey = ((FormWebAuthenticationDetails) authentication.getDetails()).getSecretKey();

        if (!secretKey.equals("secret")) {
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }

        return new UsernamePasswordAuthenticationToken(
                accountContext.getAccount(),
                null,
                accountContext.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
