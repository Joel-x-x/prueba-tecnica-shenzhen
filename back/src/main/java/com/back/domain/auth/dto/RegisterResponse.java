package com.back.domain.auth.dto;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String firstNames,
        String lastNames,
        String email
) {
}
