package com.back.domain.role.dto;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name
) {
}
