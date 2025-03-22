package com.back.service.post;

import com.back.domain.post.dto.PostRequest;
import com.back.domain.post.dto.PostResponse;
import com.back.domain.post.dto.PostUpdateRequest;
import com.back.domain.post.entity.PostEntity;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<PostResponse> listPublic(Pageable pageable) {
        return this.postRepository.findAllByDeletedIsFalseAndIsPublicIsTrue(pageable)
                .map(PostResponse::new);
    }

    public Page<PostResponse> list(Pageable pageable, Authentication authentication) {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        if(this.isAdmin(user)) {
            return this.postRepository.findAllByDeletedIsFalse(pageable)
                    .map(PostResponse::new);
        } else {
            return this.postRepository.findAllByDeletedIsFalseAndUser(pageable, user)
                    .map(PostResponse::new);
        }
    }

    public PostResponse get(UUID id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        PostEntity post = findPostById(id);

        // Validate same user or admin
        if(post.getUser().getId().equals(user.getId()) || this.isAdmin(user)) {
            return new PostResponse(post);
        }
            throw new SecurityException("Unauthorized");
    }

    public PostResponse create(PostRequest request,
                                   Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        PostEntity post = this.postRepository.save(
                PostEntity.builder()
                        .title(request.title())
                        .content(request.content())
                        .isPublic(request.isPublic())
                        .user(user)
                        .build()
        );

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .isPublic(post.getIsPublic())
                .created(post.getCreatedAt())
                .userId(post.getUser().getId())
                .build();
    }

    public PostResponse update(PostUpdateRequest request, UUID id,
                                   Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        PostEntity post = findPostById(id);

        if(post.getUser().getId().equals(user.getId()) || this.isAdmin(user)) {
            if(!StringUtils.isBlank(request.title())) {
                post.setTitle(request.title());
            }

            if(!StringUtils.isBlank(request.content())) {
                post.setContent(request.content());
            }

            if(request.isPublic() != null) {
                post.setIsPublic(request.isPublic());
            }

            return new PostResponse(this.postRepository.save(post));
        }

        throw new SecurityException("Unauthorized");
    }

    public PostResponse delete(UUID id, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();

        PostEntity post = findPostById(id);

        if(post.getUser().getId().equals(user.getId()) || this.isAdmin(user)) {
            post.setDeleted(true);
            return new PostResponse(this.postRepository.save(post));
        }

        throw new SecurityException("Unauthorized");
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));
    }

    private PostEntity findPostById(UUID id) {
        return this.postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not " +
                        "found"));
    }
}
