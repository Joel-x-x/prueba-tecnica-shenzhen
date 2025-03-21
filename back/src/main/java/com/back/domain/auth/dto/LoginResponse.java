package com.back.domain.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
