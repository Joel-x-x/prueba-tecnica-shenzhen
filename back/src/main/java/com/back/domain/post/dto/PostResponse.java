package com.back.domain.post.dto;

import com.back.domain.post.entity.PostEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PostResponse(
    UUID id,
    String title,
    String content,
    Boolean isPublic,
    LocalDateTime created,
    UUID userId,
    String author
) {
    public PostResponse(PostEntity post) {
        this(post.getId(), post.getTitle(), post.getContent(), post.getIsPublic(),
                post.getCreatedAt(), post.getUser().getId(),
                post.getUser().getFirstNames() + " " + post.getUser().getLastNames()
        );
    }
}
