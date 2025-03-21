package com.back.controller.user;

import com.back.domain.user.dto.UserRequest;
import com.back.domain.user.dto.UserResponse;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.exception.ResultResponse;
import com.back.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResultResponse<Page<UserResponse>, String>> list(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.list(pageable), 200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<UserResponse, String>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.get(id), 200
                )
        );
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<UserResponse, String>> update(@RequestBody UserRequest request, @PathVariable UUID id) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.userService.update(request, id), 200
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Boolean, String>> delete(@PathVariable UUID id) {
        this.userService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}
