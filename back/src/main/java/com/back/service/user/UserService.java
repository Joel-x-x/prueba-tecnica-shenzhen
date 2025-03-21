package com.back.service.user;

import com.back.domain.role.entity.RoleEntity;
import com.back.domain.user.dto.UserRequest;
import com.back.domain.user.dto.UserResponse;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.exception.IntegrityValidation;
import com.back.infrastructure.repository.RoleRepository;
import com.back.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> list(Pageable pageable) {
        return userRepository.findAllByDeletedIsFalse(pageable)
                .map(UserResponse::new);
    }

    public UserResponse get(UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserResponse create(UserRequest request) {
        RoleEntity role = roleRepository.findByName(request.role())
                .orElseGet(() -> {
                    if(request.role().equals("USER") || request.role().equals("ADMIN")) {
                        return roleRepository.save(RoleEntity.builder()
                                .name(request.role())
                                .build());
                    } else {
                        throw new IntegrityValidation("Role not found");
                    }
                });

        Set<RoleEntity> roles = Collections.singleton(role);

        return new UserResponse(
                userRepository.save(UserEntity.builder()
                        .firstNames(request.firstNames())
                        .lastNames(request.lastNames())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .roles(roles)
                        .build())
        );
    }

    public UserResponse update(UserRequest request, UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        RoleEntity role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new IntegrityValidation("Role not found"));

        Set<RoleEntity> roles = Collections.singleton(role);

        user.setFirstNames(request.firstNames());
        user.setLastNames(request.lastNames());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);

        return new UserResponse(userRepository.save(user));
    }

    public Boolean delete(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setDeleted(true);
        userRepository.save(user);

        return true;
    }

}
