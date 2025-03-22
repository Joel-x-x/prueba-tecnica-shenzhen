package com.back.controller.post;

import com.back.domain.post.dto.PostRequest;
import com.back.domain.post.dto.PostResponse;
import com.back.domain.post.dto.PostUpdateRequest;
import com.back.infrastructure.exception.ResultResponse;
import com.back.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/public")
    public ResponseEntity<ResultResponse<Page<PostResponse>, String>> listPublic(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.postService.listPublic(pageable), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<ResultResponse<Page<PostResponse>, String>> list(
            @PageableDefault(page = 0, size = 10) Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.postService.list(pageable, authentication), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<PostResponse, String>> get(
            @PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.postService.get(id, authentication), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    public ResponseEntity<ResultResponse<PostResponse, String>> create(
            @RequestBody PostRequest request, Authentication authentication) {
        PostResponse response = this.postService.create(request, authentication);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(
                ResultResponse.success(response, 201)
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<PostResponse, String>> update(
            @RequestBody PostUpdateRequest request, @PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.postService.update(request, id, authentication), 200
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id, Authentication authentication) {
        this.postService.delete(id, authentication);

        return ResponseEntity.noContent().build();
    }
}
