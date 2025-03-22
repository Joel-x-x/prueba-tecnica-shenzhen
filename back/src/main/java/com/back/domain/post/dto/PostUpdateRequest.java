package com.back.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PostUpdateRequest(
        String title,
        String content,
        Boolean isPublic
) {
}
