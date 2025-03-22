package com.back.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

public record RegisterRequest(
        @NotBlank
        @Size(min = 10, max = 30)
        String firstNames,
        @NotBlank
        @Size(min = 10, max = 30)
        String lastNames,
        @Email
        @UniqueElements
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {
}
