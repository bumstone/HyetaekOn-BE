package com.hyetaekon.hyetaekon.post.controller;

import com.hyetaekon.hyetaekon.post.dto.PostDetailReponseDto;
import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.dto.PostListResponseDto;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import com.hyetaekon.hyetaekon.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    // ✅ 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailReponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // User, Admin에 따라 다른 접근 가능
    // ✅ 게시글 생성
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    // ✅ 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(postId, postDto));
    }

    // ✅ 게시글 삭제 (soft delete 방식 사용 가능)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
