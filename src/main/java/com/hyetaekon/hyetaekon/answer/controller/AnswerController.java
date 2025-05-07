package com.hyetaekon.hyetaekon.answer.controller;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.service.AnswerService;
import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    // 답변 작성
    @PostMapping
    public ResponseEntity<AnswerDto> createAnswer(
        @PathVariable Long postId,
        @RequestBody AnswerDto answerDto,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        AnswerDto createdAnswer = answerService.createAnswer(postId, answerDto, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    // 답변 채택
    // TODO: 질문 게시글 작성자만 가능
    @PutMapping("/{answerId}/select")
    public ResponseEntity<Void> selectAnswer(@PathVariable Long answerId) {
        answerService.selectAnswer(answerId);
        return ResponseEntity.ok().build();
    }

    // 답변 삭제 (관리자만 가능)
    @DeleteMapping("/admin/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}

