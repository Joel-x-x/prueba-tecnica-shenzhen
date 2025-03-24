package com.back.controller.user;

import com.back.domain.user.dto.UserRequest;
import com.back.domain.user.dto.UserResponse;
import com.back.domain.user.dto.UserUpdateRequest;
import com.back.infrastructure.exception.ResultResponse;
import com.back.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResultResponse<Page<UserResponse>, String>> list(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.list(pageable), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<UserResponse, String>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.get(id), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResultResponse<UserResponse, String>> create(@RequestBody UserRequest request) {
        UserResponse response = this.userService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(
                ResultResponse.success(
                        response, 201
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<UserResponse, String>> update(@RequestBody UserUpdateRequest request, @PathVariable UUID id) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.update(request, id), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.userService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}
