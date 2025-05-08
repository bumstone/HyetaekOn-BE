package com.hyetaekon.hyetaekon.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private Long parentId;  // 대댓글일 경우 부모 댓글 ID
    private String content;
    private Long userId;
    private String nickname;
    private boolean deleted;
    private LocalDateTime createdAt;
}
