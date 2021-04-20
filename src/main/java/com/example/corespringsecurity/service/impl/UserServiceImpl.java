package com.example.corespringsecurity.service.impl;

import com.example.corespringsecurity.domain.Account;
import com.example.corespringsecurity.domain.AccountDto;
import com.example.corespringsecurity.domain.Role;
import com.example.corespringsecurity.repository.UserRepository;
import com.example.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void createUser(AccountDto accountDto) {
        /* DTO 에서 바로 PasswordEncoder 이용하여, Password 변환 후 toEntity 메소드 이용해서 Entity 객체 변환 */
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));

        Account account = accountDto.toUserEntity();
        log.info("account entity : {}", account);

        userRepository.save(account);
    }

}
