package com.hyetaekon.hyetaekon.comment.controller;

import com.hyetaekon.hyetaekon.comment.dto.CommentDto;
import com.hyetaekon.hyetaekon.comment.service.CommentService;
import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글 댓글 목록 조회 (페이징 지원)
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<CommentDto>> getComments(@PathVariable Long postId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<CommentDto> comments = commentService.getComments(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    // 게시글에 댓글 작성
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(postId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // 대댓글 목록 조회
    @GetMapping("/{commentId}/replies")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<CommentDto>> getReplies(@PathVariable Long postId,
                                                       @PathVariable Long commentId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        Page<CommentDto> replies = commentService.getReplies(postId, commentId, page, size);
        return ResponseEntity.ok(replies);
    }

    // 대댓글 작성
    @PostMapping("/{commentId}/replies")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDto> createReply(@PathVariable Long postId,
                                                  @PathVariable Long commentId,
                                                  @RequestBody CommentDto commentDto) {
        CommentDto createdReply = commentService.createReply(postId, commentId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReply);
    }

    // 댓글 삭제 (관리자만 가능)
    @DeleteMapping("/admin/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String postId) {

        Long currentUserId = userDetails.getId();
        boolean isAdmin = "ROLE_ADMIN".equals(userDetails.getRole());

        commentService.deleteComment(commentId, currentUserId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}
