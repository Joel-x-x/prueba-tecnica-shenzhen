package com.back.domain.role.dto;

import com.back.domain.role.entity.RoleEntity;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResponse(
        UUID id,
        String name
) {
    public RoleResponse(RoleEntity role) {
        this(role.getId(), role.getName());
    }
}
