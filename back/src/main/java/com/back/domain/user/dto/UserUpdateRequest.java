package com.back.domain.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String firstNames,
        String lastNames,
        String role
) {
}
