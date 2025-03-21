package com.back.domain.user.dto;

import com.back.domain.role.dto.RoleResponse;
import com.back.domain.user.entity.UserEntity;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record UserResponse(
        UUID id,
        String email,
        String firstNames,
        String lastNames,
        Set<RoleResponse> roles
) {
    public UserResponse(UserEntity userEntity) {
        this(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstNames(),
                userEntity.getLastNames(),
                userEntity.getRoles()
                        .stream().map(RoleResponse::new)
                        .collect(Collectors.toSet())
        );
    }
}
