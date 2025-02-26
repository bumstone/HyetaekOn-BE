package com.hyetaekon.hyetaekon.post.controller;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 목록 조회
    @GetMapping("/posts/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPosts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPosts(categoryId, page, size));
    }

    // 게시글 상세 조회
    @GetMapping("/posts/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 게시글 작성 (USER)
    @PostMapping("/posts/category/{categoryId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostDto> createPost(
            @PathVariable Long categoryId, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(categoryId, postDto));
    }

    // 게시글 삭제 (USER)
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 작성 (ADMIN)
    @PostMapping("/admin/posts/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> createPostAdmin(
            @PathVariable Long categoryId, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(categoryId, postDto));
    }

    // 게시글 삭제 (ADMIN, 복수 삭제 가능)
    @DeleteMapping("/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePosts(@RequestParam List<Long> postId) {
        postService.deletePosts(postId);
        return ResponseEntity.noContent().build();
    }
}
