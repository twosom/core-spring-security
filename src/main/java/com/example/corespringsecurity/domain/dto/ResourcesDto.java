package com.example.corespringsecurity.domain.dto;

import com.example.corespringsecurity.domain.entity.Resources;
import com.example.corespringsecurity.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDto {

    private String id;
    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;
    private String roleName;
    private Set<Role> roleSet;

    public Resources toEntity() {
        return Resources.builder()
                .resourceName(getResourceName())
                .httpMethod(getHttpMethod())
                .orderNum(getOrderNum())
                .resourceType(getResourceType())
                .roleSet(getRoleSet())
                .build();
    }
}
