package com.back.controller.auth;

import com.back.domain.auth.dto.*;
import com.back.infrastructure.exception.ResultResponse;
import com.back.infrastructure.jwt.JwtService;
import com.back.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResultResponse<RegisterResponse,String>> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.authService.register(request), 201)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResultResponse<LoginResponse,String>> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(ResultResponse.success(
                this.authService.login(request), 200));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResultResponse<RefreshTokenResponse, Object>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ResultResponse.success(
                this.authService.refreshToken(request), 200
        ));
    }
}
