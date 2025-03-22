package com.back.infrastructure.repository;

import com.back.domain.post.entity.PostEntity;
import com.back.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    Page<PostEntity> findAllByDeletedIsFalse(Pageable pageable);
    Page<PostEntity> findAllByDeletedIsFalseAndUser(Pageable pageable,
                                                    UserEntity user);
    Page<PostEntity> findAllByDeletedIsFalseAndIsPublicIsTrue(Pageable pageable);
}
