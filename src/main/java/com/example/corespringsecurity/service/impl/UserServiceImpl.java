package com.example.corespringsecurity.service.impl;

import com.example.corespringsecurity.domain.entity.Account;
import com.example.corespringsecurity.domain.dto.AccountDto;
import com.example.corespringsecurity.domain.entity.Role;
import com.example.corespringsecurity.repository.RoleRepository;
import com.example.corespringsecurity.repository.UserRepository;
import com.example.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    public void createUser(AccountDto accountDto) {

        Role role = roleRepository.findByRoleName("ROLE_USER");

        /* DTO 에서 바로 PasswordEncoder 이용하여, Password 변환 후 toEntity 메소드 이용해서 Entity 객체 변환 */
        accountDto.setPassword(encoder.encode(accountDto.getPassword()));
        Account account = accountDto.toEntity();
        account.addRole(role);

        userRepository.save(account);
    }

    @Override
    public void modifyUser(AccountDto accountDto) {
        /* username 과 id 중에 뭐로 찾을 지 결정하기 */
        Account findAccount = userRepository.findByUsername(accountDto.getUsername());


        accountDto.setPassword(encoder.encode(accountDto.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        extractRoles(accountDto, roles);

        /* DirtyChecking 을 이용한 Update */
        if (roles.size() > 0) {
            findAccount.update(accountDto, roles);
        } else {
            findAccount.update(accountDto);
        }
    }

    private void extractRoles(AccountDto accountDto, HashSet<Role> roles) {
        if (accountDto.getRoles() != null) {
            accountDto.getRoles().forEach(role -> {
                Role findRole = roleRepository.findByRoleName(role);
                roles.add(findRole);
            });
        }
    }

    @Override
    public List<Account> getUsers() {
        List<Account> all = userRepository.findAll();
        all.forEach(account -> account.getUserRoles().forEach(Role::getRoleName));
        return all;
    }

    @Override
    public AccountDto getUser(Long id) {
        Account account = userRepository.findById(id).orElse(new Account());
        AccountDto accountDto = mapper.map(account, AccountDto.class);

        List<String> roles = account.getUserRoles()
                .stream().map(role -> role.getRoleName())
                .collect(Collectors.toList());
        accountDto.setRoles(roles);

        return accountDto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
