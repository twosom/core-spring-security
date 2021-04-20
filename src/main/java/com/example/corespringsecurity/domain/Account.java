package com.example.corespringsecurity.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity @EntityListeners(AuditingEntityListener.class)
@Getter @NoArgsConstructor @ToString(of = {"username", "password", "email", "age", "role"})
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private String age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public Account(String username, String password, String email, String age, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.role = role;
    }
}
