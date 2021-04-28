package com.example.corespringsecurity.domain.dto;

import com.example.corespringsecurity.domain.entity.Account;
import com.example.corespringsecurity.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String id;
    private String username;
    private String email;
    private int age;
    private String password;
    private List<String> roles;


    public Account toEntity() {
        return Account.
                builder()
                .username(getUsername())
                .email(getEmail())
                .age(getAge())
                .password(getPassword())
                .build();
    }
}
