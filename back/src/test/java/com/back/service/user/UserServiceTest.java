package com.back.service.user;

import com.back.domain.role.entity.RoleEntity;
import com.back.domain.user.dto.UserRequest;
import com.back.domain.user.dto.UserResponse;
import com.back.domain.user.dto.UserUpdateRequest;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.repository.RoleRepository;
import com.back.infrastructure.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @InjectMocks
    UserService userService;

    private UserEntity user;
    private RoleEntity role;
    private UserRequest request;

    @BeforeEach
    void setUp() {
        role = new RoleEntity("ADMIN");

        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setFirstNames("Admin");
        user.setLastNames("Monadmin");
        user.setEmail("admin@gmail.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(role));

        request = new UserRequest("admin@gmail.com", "password123", "Admin",
                "Monadmin", role.getName());
    }

    @Test
    void list() {
        List<UserEntity> users = List.of(user);
        Page<UserEntity> page = new PageImpl<>(users);

        when(userRepository.findAllByDeletedIsFalse(any(Pageable.class))).thenReturn(page);

        Page<UserResponse> result = userService.list(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(user.getEmail(), result.getContent().get(0).email());
    }

    @Test
    void get() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse response = userService.get(user.getId());

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getFirstNames(), response.firstNames());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getWhenUserDoesNotExists() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get(UUID.randomUUID()));

        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void create() {
        when(roleRepository.findByName(request.role())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals(request.email(), result.email());
        assertEquals(request.firstNames(), result.firstNames());
        assertEquals(request.lastNames(), result.lastNames());
    }

    @Test
    void update() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserResponse result =
                userService.update(UserUpdateRequest.builder()
                        .firstNames(request.firstNames())
                        .lastNames(request.lastNames())
                        .build(),
                user.getId());

        assertNotNull(result);
        assertEquals(request.firstNames(), result.firstNames());
        assertEquals(request.lastNames(), result.lastNames());
    }

    @Test
    void delete() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepository).save(userCaptor.capture());
        UserEntity deletedUser = userCaptor.getValue();

        assertTrue(deletedUser.getDeleted());
    }
}