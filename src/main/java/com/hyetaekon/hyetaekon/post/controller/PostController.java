package com.hyetaekon.hyetaekon.post.controller;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // ✅ 게시글 생성
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    // ✅ 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // ✅ 특정 카테고리의 게시글 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(postService.getPostsByCategoryId(categoryId));
    }

    // ✅ 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
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
