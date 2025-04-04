package com.back.infrastructure.repository;

import com.back.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Page<UserEntity> findAllByDeletedIsFalse(Pageable pageable);
    Boolean existsUserEntitiesByEmail(String email);
}
