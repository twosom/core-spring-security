package com.example.corespringsecurity.domain;

import lombok.Data;

@Data
public class AccountDto {

    private String username;
    private String password;
    private String email;
    private String age;
    private Role role;

    private String adminName;


    public Account toUserEntity() {
        return Account.builder()
                .username(getUsername())
                .password(getPassword())
                .email(getEmail())
                .age(getAge())
                .role(getRole())
                .build();
    }


}
