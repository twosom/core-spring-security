package com.example.corespringsecurity.repository;


import com.example.corespringsecurity.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminAccount, Long> {

    AdminAccount findByUsername(String username);
}
