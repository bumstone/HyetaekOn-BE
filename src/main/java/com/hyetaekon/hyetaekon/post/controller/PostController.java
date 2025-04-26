package com.hyetaekon.hyetaekon.post.controller;

import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import com.hyetaekon.hyetaekon.post.dto.*;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import com.hyetaekon.hyetaekon.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 전체 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<PostListResponseDto>> getAllPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PostListResponseDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // PostType에 해당하는 게시글 목록 조회
    @GetMapping("/type/{postType}")
    public ResponseEntity<Page<PostListResponseDto>> getPostsByType(
        @PathVariable PostType postType,
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PostListResponseDto> posts = postService.getPostsByType(postType, pageable);
        return ResponseEntity.ok(posts);
    }

    // User, Admin에 따라 다른 접근 가능
    // ✅ 특정 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(postService.getPostById(postId, userDetails.getId()));
    }

    // ✅ 게시글 생성
    @PostMapping
    public ResponseEntity<PostDetailResponseDto> createPost(
        @RequestBody PostCreateRequestDto requestDto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(postService.createPost(requestDto, userDetails.getId()));
    }

    // ✅ 게시글 수정 - 본인
    @PutMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> updatePost(
        @PathVariable Long postId,
        @RequestBody PostUpdateRequestDto updateDto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(postService.updatePost(postId, updateDto, userDetails.getId()));
    }

    // ✅ 게시글 삭제 (soft delete 방식 사용 가능) - 본인 혹은 관리자
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(postId, userDetails.getId(), userDetails.getRole());
        return ResponseEntity.noContent().build();
    }
}
