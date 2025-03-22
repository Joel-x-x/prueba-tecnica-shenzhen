package com.back.service.auth;

import com.back.domain.auth.dto.*;
import com.back.domain.role.entity.RoleEntity;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.exception.IntegrityValidation;
import com.back.infrastructure.jwt.JwtService;
import com.back.infrastructure.repository.RoleRepository;
import com.back.infrastructure.repository.UserRepository;
import com.back.service.auth.validation.RegisterAuthValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private List<RegisterAuthValidation> registerAuthValidations;

    @InjectMocks
    private AuthService authService;

    private UserEntity user;
    private RoleEntity role;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        role = new RoleEntity("USER");

        user = UserEntity.builder()
                .id(UUID.randomUUID())
                .firstNames("Marco")
                .lastNames("Cordoba")
                .email("marco@gmail.com")
                .password("marco@gmail.com")
                .roles(Set.of(role))
                .build();

        registerRequest = new RegisterRequest(user.getFirstNames(), user.getLastNames(),
                user.getEmail(), user.getPassword());
        loginRequest = new LoginRequest(user.getEmail() , user.getPassword());
    }

    @Test
    void registerSuccess() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        RegisterResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getEmail(), response.email());

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void loginSuccess() {
        when(userRepository.findByEmailIgnoreCase(loginRequest.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("mockRefreshToken");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mockAccessToken", response.accessToken());
        assertEquals("mockRefreshToken", response.refreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginFailureInvalidCredentials() {
        when(userRepository.findByEmailIgnoreCase(loginRequest.email())).thenReturn(Optional.empty());

        assertThrows(IntegrityValidation.class, () -> authService.login(loginRequest));

        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void refreshTokenSuccess() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("mockRefreshToken");
        when(jwtService.extractUsername("mockRefreshToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.validateToken("mockRefreshToken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");

        RefreshTokenResponse response = authService.refreshToken(refreshTokenRequest);

        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals("mockRefreshToken", response.refreshToken());

        verify(jwtService).generateToken(user);
    }

    @Test
    void refreshTokenFailureInvalidToken() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenReturn(null);

        assertThrows(IntegrityValidation.class, () -> authService.refreshToken(refreshTokenRequest));

        verify(userRepository, never()).findByEmailIgnoreCase(anyString());
        verify(jwtService, never()).generateToken(any(UserEntity.class));
    }

    @Test
    void refreshTokenFailureUserNotFound() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("mockRefreshToken");
        when(jwtService.extractUsername("mockRefreshToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authService.refreshToken(refreshTokenRequest));

        verify(jwtService, never()).generateToken(any(UserEntity.class));
    }
}
