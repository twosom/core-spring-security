package com.example.corespringsecurity.service;

import com.example.corespringsecurity.domain.Account;
import com.example.corespringsecurity.domain.AccountDto;

public interface UserService {



    void createUser(AccountDto accountDto);
}
