package com.example.corespringsecurity.domain.entity;

import com.example.corespringsecurity.domain.dto.AccountDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Builder
@Entity @EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Getter @NoArgsConstructor @ToString(of = {"username", "password", "email", "age"})
public class Account implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private int age;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> userRoles = new HashSet<>();

    public void addRole(Role role) {
        if (getUserRoles() == null) {
            this.userRoles = new HashSet<>();
        }
        getUserRoles().add(role);
    }

    public void resetPassword(String password) {
        this.password = password;
    }


    public void update(AccountDto accountDto, HashSet<Role> roles) {
        this.userRoles = roles;
        this.password = accountDto.getPassword();
    }

    public void update(AccountDto accountDto) {
        this.password = accountDto.getPassword();
    }
}
