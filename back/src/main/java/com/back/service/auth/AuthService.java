package com.back.service.auth;

import com.back.domain.auth.dto.*;
import com.back.domain.role.entity.RoleEntity;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.exception.IntegrityValidation;
import com.back.infrastructure.jwt.JwtService;
import com.back.infrastructure.repository.RoleRepository;
import com.back.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String ROLE_USER = "USER";


    public RegisterResponse register(RegisterRequest request) {
        RoleEntity role = roleRepository.findByName(ROLE_USER)
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                        .name(ROLE_USER)
                        .build()));

        Set<RoleEntity> roles = Collections.singleton(role);

        UserEntity user = this.userRepository.save(UserEntity.builder()
                .firstNames(request.firstNames())
                .lastNames(request.lastNames())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .build());

        return new RegisterResponse(
                user.getId(),
                user.getFirstNames(),
                user.getLastNames(),
                user.getEmail()
        );
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.refreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            UserEntity user = userRepository.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (jwtService.validateToken(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return new RefreshTokenResponse(
                        accessToken,
                        refreshToken
                );
            }
        }

        throw new IntegrityValidation("Invalid refresh token");
    }

    public LoginResponse login(LoginRequest request) {
        return userRepository.findByEmailIgnoreCase(request.email())
                .map( user -> {
                   authenticationManager.authenticate(
                           new UsernamePasswordAuthenticationToken(
                                      request.email(),
                                      request.password()
                           )
                   );

                   String accessToken = jwtService.generateToken(user);
                   String refreshToken = jwtService.generateRefreshToken(user);

                   return new LoginResponse(
                           accessToken,
                           refreshToken
                   );
                }).orElseThrow(() -> new IntegrityValidation("Invalid credentials"));
    }

}
