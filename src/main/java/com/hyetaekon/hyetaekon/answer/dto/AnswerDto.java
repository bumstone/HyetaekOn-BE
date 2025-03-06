package com.hyetaekon.hyetaekon.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long postId;
    private String content;
    private boolean selected;
    private LocalDateTime createdAt;
}
