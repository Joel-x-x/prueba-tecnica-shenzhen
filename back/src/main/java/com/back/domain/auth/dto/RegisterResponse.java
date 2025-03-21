package com.back.domain.auth.dto;

import com.back.domain.role.dto.RoleResponse;

import java.util.Set;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String firstNames,
        String lastNames,
        String email
) {
}
