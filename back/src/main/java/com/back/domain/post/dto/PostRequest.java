package com.back.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank
        @Size(min = 1, max = 50)
        String title,
        @NotBlank
        @Size(min = 1, max = 1000)
        String content,
        @NotNull
        Boolean isPublic
) {
}
