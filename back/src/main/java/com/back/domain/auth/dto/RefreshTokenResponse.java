package com.back.domain.auth.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
