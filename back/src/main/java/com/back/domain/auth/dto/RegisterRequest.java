package com.back.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 10, max = 30)
        String firstNames,
        @NotBlank
        @Size(min = 10, max = 30)
        String lastNames,
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {
}
