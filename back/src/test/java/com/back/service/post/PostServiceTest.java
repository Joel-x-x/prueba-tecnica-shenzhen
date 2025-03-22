package com.back.service.post;

import com.back.domain.post.dto.PostRequest;
import com.back.domain.post.dto.PostResponse;
import com.back.domain.post.dto.PostUpdateRequest;
import com.back.domain.post.entity.PostEntity;
import com.back.domain.user.entity.UserEntity;
import com.back.domain.role.entity.RoleEntity;
import com.back.infrastructure.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private Authentication authentication;
    @InjectMocks private PostService postService;
    @Captor private ArgumentCaptor<PostEntity> postCaptor;

    private UserEntity user;
    private PostEntity post;
    private PostRequest request;
    private UUID postId;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setRoles(Set.of(new RoleEntity("USER")));

        post = PostEntity.builder()
                .id(postId)
                .title("Test Title")
                .content("Test Content")
                .isPublic(true)
                .user(user)
                .deleted(false)
                .build();

        lenient().when(authentication.getPrincipal()).thenReturn(user);

        request = new PostRequest(post.getTitle(), post.getContent(), post.getIsPublic());
    }

    @Test
    void listPublic() {
        List<PostEntity> posts = List.of(post);
        Page<PostEntity> page = new PageImpl<>(posts);
        when(postRepository.findAllByDeletedIsFalseAndIsPublicIsTrue(any(Pageable.class))).thenReturn(page);

        Page<PostResponse> result = postService.listPublic(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(post.getTitle(), result.getContent().get(0).title());
    }

    @Test
    void list() {
        List<PostEntity> posts = List.of(post);
        Page<PostEntity> page = new PageImpl<>(posts);
        when(postRepository.findAllByDeletedIsFalseAndUser(any(Pageable.class), eq(user))).thenReturn(page);

        Page<PostResponse> result = postService.list(PageRequest.of(0, 10), authentication);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Title", result.getContent().get(0).title());
    }

    @Test
    void get() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostResponse result = postService.get(postId, authentication);

        assertNotNull(result);
        assertEquals(postId, result.id());
        assertEquals("Test Title", result.title());
    }

    @Test
    void getUnauthorized() {
        UserEntity otherUser = new UserEntity();
        otherUser.setId(UUID.randomUUID());
        post.setUser(otherUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(SecurityException.class, () -> postService.get(postId, authentication));
    }

    @Test
    void create() {
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostResponse result = postService.create(request, authentication);

        assertNotNull(result);
        assertEquals(post.getTitle(), result.title());
    }

    @Test
    void update() {
        PostUpdateRequest request = new PostUpdateRequest("Updated Title", "Updated Content", false);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostResponse result = postService.update(request, postId, authentication);

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
        assertFalse(result.isPublic());
    }

    @Test
    void updateUnauthorized() {
        UserEntity otherUser = new UserEntity();
        otherUser.setId(UUID.randomUUID());
        post.setUser(otherUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(SecurityException.class, () -> postService.update(new PostUpdateRequest("Title", "Content", true), postId, authentication));
    }

    @Test
    void delete() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.delete(postId, authentication);

        verify(postRepository).save(postCaptor.capture());
        assertTrue(postCaptor.getValue().getDeleted());
    }

    @Test
    void delete_Unauthorized() {
        UserEntity otherUser = new UserEntity();
        otherUser.setId(UUID.randomUUID());
        post.setUser(otherUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(SecurityException.class, () -> postService.delete(postId, authentication));
    }
}
