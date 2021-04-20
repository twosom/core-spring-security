package com.example.corespringsecurity.domain;


import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter @NoArgsConstructor
public class AdminAccount extends Account {
    private String adminName;



    public AdminAccount(AccountDto accountDto) {
        super(accountDto.getUsername(),
                accountDto.getPassword(),
                accountDto.getEmail(),
                accountDto.getAge(),
                accountDto.getRole());
        this.adminName = accountDto.getAdminName();
    }
}
